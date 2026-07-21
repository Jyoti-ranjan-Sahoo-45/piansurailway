/* ==========================================================
   RailBook Pro - LocalStorage Data Store
   Acts as a temporary backend. All CRUD persists to LocalStorage.
   Seeded with the same 5 sample records as the backend.
   ========================================================== */
(function (global) {
    'use strict';

    const KEY = 'railbookpro_db';
    const VERSION = 1;

    // ---------- Seed data ----------
    function seed() {
        return {
            version: VERSION,
            users: [
                { id: 1, username: 'admin', fullName: 'System Administrator', email: 'admin@railbookpro.com', phone: '9000000000', password: 'admin123', gender: 'MALE', active: true, role: 'ADMIN', createdAt: now() },
                { id: 2, username: 'rahul', fullName: 'Rahul Sharma', email: 'rahul@example.com', phone: '9811111111', password: 'password123', gender: 'MALE', active: true, role: 'PASSENGER', createdAt: now() },
                { id: 3, username: 'priya', fullName: 'Priya Patel', email: 'priya@example.com', phone: '9822222222', password: 'password123', gender: 'FEMALE', active: true, role: 'PASSENGER', createdAt: now() },
                { id: 4, username: 'arjun', fullName: 'Arjun Reddy', email: 'arjun@example.com', phone: '9833333333', password: 'password123', gender: 'MALE', active: true, role: 'PASSENGER', createdAt: now() },
                { id: 5, username: 'sneha', fullName: 'Sneha Iyer', email: 'sneha@example.com', phone: '9844444444', password: 'password123', gender: 'FEMALE', active: true, role: 'PASSENGER', createdAt: now() }
            ],
            stations: [
                { id: 1, code: 'BBS', name: 'Bhubaneswar', city: 'Bhubaneswar', state: 'Odisha', platformCount: 6 },
                { id: 2, code: 'HWH', name: 'Howrah Junction', city: 'Howrah', state: 'West Bengal', platformCount: 23 },
                { id: 3, code: 'NDLS', name: 'New Delhi', city: 'New Delhi', state: 'Delhi', platformCount: 16 },
                { id: 4, code: 'MAS', name: 'Chennai Central', city: 'Chennai', state: 'Tamil Nadu', platformCount: 12 },
                { id: 5, code: 'CSMT', name: 'Mumbai CSMT', city: 'Mumbai', state: 'Maharashtra', platformCount: 18 },
                { id: 6, code: 'SBC', name: 'KSR Bengaluru', city: 'Bengaluru', state: 'Karnataka', platformCount: 10 }
            ],
            trains: [
                { id: 1, trainNumber: '22952', name: 'Mumbai Rajdhani', type: 'RAJDHANI', sourceStationId: 5, destinationStationId: 3, totalSeats: 900, availableSeats: 900, fare: 2500, runningDays: 'MON,TUE,WED,THU,FRI,SAT,SUN', status: 'ACTIVE' },
                { id: 2, trainNumber: '12841', name: 'Coromandel Express', type: 'SUPERFAST', sourceStationId: 2, destinationStationId: 4, totalSeats: 1200, availableSeats: 1200, fare: 1200, runningDays: 'MON,TUE,WED,THU,FRI,SAT,SUN', status: 'ACTIVE' },
                { id: 3, trainNumber: '20817', name: 'Bhubaneswar Rajdhani', type: 'RAJDHANI', sourceStationId: 1, destinationStationId: 3, totalSeats: 800, availableSeats: 800, fare: 2200, runningDays: 'MON,WED,FRI,SUN', status: 'ACTIVE' },
                { id: 4, trainNumber: '12301', name: 'Howrah Rajdhani', type: 'RAJDHANI', sourceStationId: 2, destinationStationId: 3, totalSeats: 850, availableSeats: 850, fare: 2400, runningDays: 'MON,TUE,WED,THU,FRI,SAT,SUN', status: 'ACTIVE' },
                { id: 5, trainNumber: '12626', name: 'Kerala Express', type: 'SUPERFAST', sourceStationId: 3, destinationStationId: 4, totalSeats: 1400, availableSeats: 1400, fare: 1500, runningDays: 'MON,TUE,WED,THU,FRI,SAT,SUN', status: 'ACTIVE' }
            ],
            routes: [
                { id: 1, trainId: 1, sourceStationId: 5, destinationStationId: 3, distanceKm: 1385, intermediateStations: 'Vadodara, Kota' },
                { id: 2, trainId: 2, sourceStationId: 2, destinationStationId: 4, distanceKm: 1660, intermediateStations: 'Bhubaneswar, Vijayawada' },
                { id: 3, trainId: 3, sourceStationId: 1, destinationStationId: 3, distanceKm: 1748, intermediateStations: 'Kharagpur, Kanpur' },
                { id: 4, trainId: 4, sourceStationId: 2, destinationStationId: 3, distanceKm: 1451, intermediateStations: 'Dhanbad, Kanpur' },
                { id: 5, trainId: 5, sourceStationId: 3, destinationStationId: 4, distanceKm: 2135, intermediateStations: 'Bhopal, Nagpur, Vijayawada' }
            ],
            schedules: [
                { id: 1, trainId: 1, departureTime: '17:00', arrivalTime: '08:35', duration: '15h 35m', platform: 5, runningDays: 'DAILY' },
                { id: 2, trainId: 2, departureTime: '14:50', arrivalTime: '17:00', duration: '02h 10m', platform: 12, runningDays: 'DAILY' },
                { id: 3, trainId: 3, departureTime: '10:15', arrivalTime: '10:10', duration: '23h 55m', platform: 3, runningDays: 'MON,WED,FRI,SUN' },
                { id: 4, trainId: 4, departureTime: '16:50', arrivalTime: '10:00', duration: '17h 10m', platform: 8, runningDays: 'DAILY' },
                { id: 5, trainId: 5, departureTime: '11:25', arrivalTime: '20:40', duration: '09h 15m', platform: 4, runningDays: 'DAILY' }
            ],
            bookings: [
                { id: 1, pnr: '2451234567', userId: 2, trainId: 1, journeyDate: addDays(5), travelClass: 'AC2', seatCount: 2, totalFare: 11000, status: 'CONFIRMED', bookedAt: now(), cancelledAt: null,
                  passengers: [ { name: 'Rahul Sharma', age: 30, gender: 'MALE', seatNumber: 'AC2-1' }, { name: 'Anita Sharma', age: 28, gender: 'FEMALE', seatNumber: 'AC2-2' } ] },
                { id: 2, pnr: '2451234568', userId: 3, trainId: 2, journeyDate: addDays(2), travelClass: 'SL', seatCount: 1, totalFare: 1200, status: 'CONFIRMED', bookedAt: now(), cancelledAt: null,
                  passengers: [ { name: 'Priya Patel', age: 25, gender: 'FEMALE', seatNumber: 'SL-1' } ] },
                { id: 3, pnr: '2451234569', userId: 4, trainId: 4, journeyDate: addDays(-3), travelClass: 'AC3', seatCount: 1, totalFare: 3840, status: 'COMPLETED', bookedAt: now(), cancelledAt: null,
                  passengers: [ { name: 'Arjun Reddy', age: 35, gender: 'MALE', seatNumber: 'AC3-1' } ] },
                { id: 4, pnr: '2451234570', userId: 2, trainId: 5, journeyDate: addDays(10), travelClass: 'CC', seatCount: 3, totalFare: 5850, status: 'CONFIRMED', bookedAt: now(), cancelledAt: null,
                  passengers: [ { name: 'Rahul Sharma', age: 30, gender: 'MALE', seatNumber: 'CC-1' }, { name: 'Meena Sharma', age: 55, gender: 'FEMALE', seatNumber: 'CC-2' }, { name: 'Raj Sharma', age: 12, gender: 'MALE', seatNumber: 'CC-3' } ] },
                { id: 5, pnr: '2451234571', userId: 5, trainId: 3, journeyDate: addDays(1), travelClass: 'AC1', seatCount: 1, totalFare: 7700, status: 'CANCELLED', bookedAt: now(), cancelledAt: now(),
                  passengers: [ { name: 'Sneha Iyer', age: 27, gender: 'FEMALE', seatNumber: 'AC1-1' } ] }
            ],
            notifications: [
                { id: 1, userId: 2, title: 'Booking Successful', message: 'Your ticket is confirmed. PNR: 2451234567 for Mumbai Rajdhani.', readFlag: false, createdAt: now() },
                { id: 2, userId: 3, title: 'Booking Successful', message: 'Your ticket is confirmed. PNR: 2451234568 for Coromandel Express.', readFlag: false, createdAt: now() },
                { id: 3, userId: 4, title: 'Trip Completed', message: 'Your journey on Howrah Rajdhani is complete. Thank you!', readFlag: true, createdAt: now() },
                { id: 4, userId: 2, title: 'Booking Successful', message: 'Your ticket is confirmed. PNR: 2451234570 for Kerala Express.', readFlag: false, createdAt: now() },
                { id: 5, userId: 5, title: 'Ticket Cancelled', message: 'Your ticket with PNR 2451234571 has been cancelled.', readFlag: false, createdAt: now() }
            ],
            favouriteRoutes: [
                { id: 1, userId: 2, from: 'Mumbai CSMT', to: 'New Delhi' },
                { id: 2, userId: 2, from: 'New Delhi', to: 'Chennai Central' }
            ],
            counters: { users: 5, stations: 6, trains: 5, routes: 5, schedules: 5, bookings: 5, notifications: 5, favouriteRoutes: 2 }
        };
    }

    function now() { return new Date().toISOString(); }
    function addDays(d) {
        const dt = new Date();
        dt.setDate(dt.getDate() + d);
        return dt.toISOString().slice(0, 10);
    }

    // ---------- Persistence ----------
    let db = null;

    function load() {
        if (db) return db;
        const raw = localStorage.getItem(KEY);
        if (raw) {
            try {
                db = JSON.parse(raw);
                if (db.version === VERSION) return db;
            } catch (e) { /* fall through to reseed */ }
        }
        db = seed();
        save();
        return db;
    }

    function save() { localStorage.setItem(KEY, JSON.stringify(db)); }

    function nextId(collection) {
        load();
        db.counters[collection] = (db.counters[collection] || 0) + 1;
        return db.counters[collection];
    }

    // ---------- Generic collection API ----------
    function all(collection) { return load()[collection].slice(); }

    function find(collection, id) {
        return load()[collection].find(function (x) { return x.id === Number(id); }) || null;
    }

    function insert(collection, obj) {
        load();
        obj.id = nextId(collection);
        db[collection].push(obj);
        save();
        return obj;
    }

    function update(collection, id, patch) {
        load();
        const item = db[collection].find(function (x) { return x.id === Number(id); });
        if (!item) return null;
        Object.assign(item, patch);
        save();
        return item;
    }

    function remove(collection, id) {
        load();
        const idx = db[collection].findIndex(function (x) { return x.id === Number(id); });
        if (idx === -1) return false;
        db[collection].splice(idx, 1);
        save();
        return true;
    }

    function reset() {
        db = seed();
        save();
        return db;
    }

    global.Store = {
        load: load,
        save: save,
        all: all,
        find: find,
        insert: insert,
        update: update,
        remove: remove,
        nextId: nextId,
        reset: reset,
        now: now,
        addDays: addDays
    };
})(window);
