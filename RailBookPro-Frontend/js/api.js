/* ==========================================================
   RailBook Pro - Service Layer (over LocalStorage Store)
   Mirrors the backend business logic so pages call a clean API.
   When a real backend is wired in later, swap these bodies for fetch().
   ========================================================== */
(function (global) {
    'use strict';

    const FARE_MULTIPLIER = { SL: 1.0, CC: 1.3, AC3: 1.6, AC2: 2.2, EC: 2.8, AC1: 3.5 };
    const CLASS_LABELS = { SL: 'Sleeper', CC: 'Chair Car', AC3: 'AC 3-Tier', AC2: 'AC 2-Tier', EC: 'Exec. Chair', AC1: 'AC First Class' };

    function stationName(id) {
        const s = Store.find('stations', id);
        return s ? s.name : '-';
    }
    function stationCode(id) {
        const s = Store.find('stations', id);
        return s ? s.code : '-';
    }

    // ---------- Trains ----------
    function trainsEnriched() {
        return Store.all('trains').map(enrichTrain);
    }
    function enrichTrain(t) {
        return Object.assign({}, t, {
            sourceStationName: stationName(t.sourceStationId),
            sourceStationCode: stationCode(t.sourceStationId),
            destinationStationName: stationName(t.destinationStationId),
            destinationStationCode: stationCode(t.destinationStationId)
        });
    }

    function searchTrains(filters) {
        filters = filters || {};
        return trainsEnriched().filter(function (t) {
            if (filters.source && t.sourceStationId !== Number(filters.source)) return false;
            if (filters.destination && t.destinationStationId !== Number(filters.destination)) return false;
            if (filters.trainNumber && t.trainNumber.indexOf(filters.trainNumber.trim()) === -1) return false;
            if (filters.trainName && t.name.toLowerCase().indexOf(filters.trainName.trim().toLowerCase()) === -1) return false;
            return true;
        });
    }

    function scheduleForTrain(trainId) {
        return Store.all('schedules').find(function (s) { return s.trainId === Number(trainId); }) || null;
    }

    // ---------- Bookings ----------
    function generatePnr() {
        let pnr;
        const existing = Store.all('bookings');
        do {
            pnr = String(Math.floor(1000000000 + Math.random() * 8999999999));
        } while (existing.some(function (b) { return b.pnr === pnr; }));
        return pnr;
    }

    function createBooking(data) {
        const user = Auth.currentUser();
        if (!user) return { ok: false, error: 'Not logged in' };

        const train = Store.find('trains', data.trainId);
        if (!train) return { ok: false, error: 'Train not found' };
        if (!FARE_MULTIPLIER[data.travelClass]) return { ok: false, error: 'Invalid travel class' };
        if (data.passengers.length !== data.seatCount) return { ok: false, error: 'Passenger count must match seat count' };
        if (train.availableSeats < data.seatCount) return { ok: false, error: 'Only ' + train.availableSeats + ' seats available' };

        const totalFare = train.fare * FARE_MULTIPLIER[data.travelClass] * data.seatCount;
        let seatSeq = train.totalSeats - train.availableSeats + 1;
        const passengers = data.passengers.map(function (p) {
            return { name: p.name, age: Number(p.age), gender: p.gender, seatNumber: data.travelClass + '-' + (seatSeq++) };
        });

        const booking = Store.insert('bookings', {
            pnr: generatePnr(),
            userId: user.id,
            trainId: train.id,
            journeyDate: data.journeyDate,
            travelClass: data.travelClass,
            seatCount: data.seatCount,
            totalFare: totalFare,
            status: 'CONFIRMED',
            bookedAt: Store.now(),
            cancelledAt: null,
            passengers: passengers
        });

        Store.update('trains', train.id, { availableSeats: train.availableSeats - data.seatCount });
        addNotification(user.id, 'Booking Successful', 'Your ticket is confirmed. PNR: ' + booking.pnr + ' for ' + train.name + '.');
        return { ok: true, booking: enrichBooking(booking) };
    }

    function cancelBooking(id) {
        const booking = Store.find('bookings', id);
        if (!booking) return { ok: false, error: 'Booking not found' };
        if (booking.status === 'CANCELLED') return { ok: false, error: 'Booking already cancelled' };

        Store.update('bookings', id, { status: 'CANCELLED', cancelledAt: Store.now() });
        const train = Store.find('trains', booking.trainId);
        if (train) Store.update('trains', train.id, { availableSeats: train.availableSeats + booking.seatCount });
        addNotification(booking.userId, 'Ticket Cancelled', 'Your ticket with PNR ' + booking.pnr + ' has been cancelled.');
        return { ok: true };
    }

    function enrichBooking(b) {
        const train = Store.find('trains', b.trainId);
        return Object.assign({}, b, {
            trainNumber: train ? train.trainNumber : '-',
            trainName: train ? train.name : '-',
            sourceStation: train ? stationName(train.sourceStationId) : '-',
            destinationStation: train ? stationName(train.destinationStationId) : '-',
            travelClassLabel: CLASS_LABELS[b.travelClass] || b.travelClass
        });
    }

    function myBookings() {
        const user = Auth.currentUser();
        if (!user) return [];
        return Store.all('bookings')
            .filter(function (b) { return b.userId === user.id; })
            .sort(function (a, b) { return new Date(b.bookedAt) - new Date(a.bookedAt); })
            .map(enrichBooking);
    }

    function allBookings() {
        return Store.all('bookings')
            .sort(function (a, b) { return new Date(b.bookedAt) - new Date(a.bookedAt); })
            .map(enrichBooking);
    }

    // ---------- Notifications ----------
    function addNotification(userId, title, message) {
        return Store.insert('notifications', {
            userId: userId, title: title, message: message, readFlag: false, createdAt: Store.now()
        });
    }

    function myNotifications() {
        const user = Auth.currentUser();
        if (!user) return [];
        return Store.all('notifications')
            .filter(function (n) { return n.userId === user.id; })
            .sort(function (a, b) { return new Date(b.createdAt) - new Date(a.createdAt); });
    }

    function markNotificationRead(id) { Store.update('notifications', id, { readFlag: true }); }
    function unreadCount() { return myNotifications().filter(function (n) { return !n.readFlag; }).length; }

    // ---------- Dashboard stats ----------
    function adminStats() {
        const bookings = Store.all('bookings');
        const today = new Date().toISOString().slice(0, 10);
        const revenue = bookings.filter(function (b) { return b.status !== 'CANCELLED'; })
            .reduce(function (sum, b) { return sum + b.totalFare; }, 0);
        return {
            totalUsers: Store.all('users').length,
            totalTrains: Store.all('trains').length,
            totalBookings: bookings.length,
            todaysTrips: bookings.filter(function (b) { return b.journeyDate === today; }).length,
            cancelledTickets: bookings.filter(function (b) { return b.status === 'CANCELLED'; }).length,
            revenue: revenue,
            activeRoutes: Store.all('routes').length
        };
    }

    function passengerStats() {
        const mine = myBookings();
        const today = new Date().toISOString().slice(0, 10);
        const user = Auth.currentUser();
        return {
            upcomingTrips: mine.filter(function (b) { return b.status === 'CONFIRMED' && b.journeyDate >= today; }).length,
            bookingHistory: mine.length,
            cancelledTickets: mine.filter(function (b) { return b.status === 'CANCELLED'; }).length,
            favouriteRoutes: Store.all('favouriteRoutes').filter(function (f) { return user && f.userId === user.id; }).length
        };
    }

    global.Api = {
        FARE_MULTIPLIER: FARE_MULTIPLIER,
        CLASS_LABELS: CLASS_LABELS,
        stationName: stationName,
        stationCode: stationCode,
        trainsEnriched: trainsEnriched,
        enrichTrain: enrichTrain,
        searchTrains: searchTrains,
        scheduleForTrain: scheduleForTrain,
        createBooking: createBooking,
        cancelBooking: cancelBooking,
        enrichBooking: enrichBooking,
        myBookings: myBookings,
        allBookings: allBookings,
        addNotification: addNotification,
        myNotifications: myNotifications,
        markNotificationRead: markNotificationRead,
        unreadCount: unreadCount,
        adminStats: adminStats,
        passengerStats: passengerStats
    };
})(window);
