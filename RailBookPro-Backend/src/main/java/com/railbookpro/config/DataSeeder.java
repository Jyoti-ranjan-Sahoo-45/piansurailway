package com.railbookpro.config;

import com.railbookpro.domain.entity.*;
import com.railbookpro.domain.enums.*;
import com.railbookpro.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "railbook.seed-data", havingValue = "true", matchIfMissing = true)
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final TrainRepository trainRepository;
    private final RouteRepository routeRepository;
    private final ScheduleRepository scheduleRepository;
    private final BookingRepository bookingRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Data already seeded - skipping.");
            return;
        }
        log.info("Seeding sample data...");

        Role adminRole = roleRepository.findByName(RoleType.ADMIN)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleType.ADMIN).build()));
        Role passengerRole = roleRepository.findByName(RoleType.PASSENGER)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleType.PASSENGER).build()));

        // ---- Users ----
        User admin = createUser("admin", "System Administrator", "admin@railbookpro.com",
                "9000000000", "admin123", "MALE", Set.of(adminRole));
        User p1 = createUser("rahul", "Rahul Sharma", "rahul@example.com",
                "9811111111", "password123", "MALE", Set.of(passengerRole));
        User p2 = createUser("priya", "Priya Patel", "priya@example.com",
                "9822222222", "password123", "FEMALE", Set.of(passengerRole));
        User p3 = createUser("arjun", "Arjun Reddy", "arjun@example.com",
                "9833333333", "password123", "MALE", Set.of(passengerRole));
        User p4 = createUser("sneha", "Sneha Iyer", "sneha@example.com",
                "9844444444", "password123", "FEMALE", Set.of(passengerRole));
        userRepository.saveAll(List.of(admin, p1, p2, p3, p4));

        // ---- Stations ----
        Station bbs = station("BBS", "Bhubaneswar", "Bhubaneswar", "Odisha", 6);
        Station hwh = station("HWH", "Howrah Junction", "Howrah", "West Bengal", 23);
        Station ndls = station("NDLS", "New Delhi", "New Delhi", "Delhi", 16);
        Station mas = station("MAS", "Chennai Central", "Chennai", "Tamil Nadu", 12);
        Station csmt = station("CSMT", "Mumbai CSMT", "Mumbai", "Maharashtra", 18);
        Station sbc = station("SBC", "KSR Bengaluru", "Bengaluru", "Karnataka", 10);
        stationRepository.saveAll(List.of(bbs, hwh, ndls, mas, csmt, sbc));

        // ---- Trains ----
        Train t1 = train("22952", "Mumbai Rajdhani", TrainType.RAJDHANI, csmt, ndls, 900, 900, 2500.0, "MON,TUE,WED,THU,FRI,SAT,SUN");
        Train t2 = train("12841", "Coromandel Express", TrainType.SUPERFAST, hwh, mas, 1200, 1200, 1200.0, "MON,TUE,WED,THU,FRI,SAT,SUN");
        Train t3 = train("20817", "Bhubaneswar Rajdhani", TrainType.RAJDHANI, bbs, ndls, 800, 800, 2200.0, "MON,WED,FRI,SUN");
        Train t4 = train("12301", "Howrah Rajdhani", TrainType.RAJDHANI, hwh, ndls, 850, 850, 2400.0, "MON,TUE,WED,THU,FRI,SAT,SUN");
        Train t5 = train("12626", "Kerala Express", TrainType.SUPERFAST, ndls, mas, 1400, 1400, 1500.0, "MON,TUE,WED,THU,FRI,SAT,SUN");
        trainRepository.saveAll(List.of(t1, t2, t3, t4, t5));

        // ---- Routes ----
        routeRepository.saveAll(List.of(
                route(t1, csmt, ndls, 1385.0, "Vadodara, Kota"),
                route(t2, hwh, mas, 1660.0, "Bhubaneswar, Vijayawada"),
                route(t3, bbs, ndls, 1748.0, "Kharagpur, Kanpur"),
                route(t4, hwh, ndls, 1451.0, "Dhanbad, Kanpur"),
                route(t5, ndls, mas, 2135.0, "Bhopal, Nagpur, Vijayawada")
        ));

        // ---- Schedules ----
        scheduleRepository.saveAll(List.of(
                schedule(t1, LocalTime.of(17, 0), LocalTime.of(8, 35), 5, "DAILY"),
                schedule(t2, LocalTime.of(14, 50), LocalTime.of(17, 0), 12, "DAILY"),
                schedule(t3, LocalTime.of(10, 15), LocalTime.of(10, 10), 3, "MON,WED,FRI,SUN"),
                schedule(t4, LocalTime.of(16, 50), LocalTime.of(10, 0), 8, "DAILY"),
                schedule(t5, LocalTime.of(11, 25), LocalTime.of(20, 40), 4, "DAILY")
        ));

        // ---- Bookings ----
        Booking b1 = booking("2451234567", p1, t1, LocalDate.now().plusDays(5), TravelClass.AC2, 2, 2500.0 * 2.2 * 2, BookingStatus.CONFIRMED,
                List.of(passenger("Rahul Sharma", 30, "MALE", "AC2-1"), passenger("Anita Sharma", 28, "FEMALE", "AC2-2")));
        Booking b2 = booking("2451234568", p2, t2, LocalDate.now().plusDays(2), TravelClass.SL, 1, 1200.0, BookingStatus.CONFIRMED,
                List.of(passenger("Priya Patel", 25, "FEMALE", "SL-1")));
        Booking b3 = booking("2451234569", p3, t4, LocalDate.now().minusDays(3), TravelClass.AC3, 1, 2400.0 * 1.6, BookingStatus.COMPLETED,
                List.of(passenger("Arjun Reddy", 35, "MALE", "AC3-1")));
        Booking b4 = booking("2451234570", p1, t5, LocalDate.now().plusDays(10), TravelClass.CC, 3, 1500.0 * 1.3 * 3, BookingStatus.CONFIRMED,
                List.of(passenger("Rahul Sharma", 30, "MALE", "CC-1"), passenger("Meena Sharma", 55, "FEMALE", "CC-2"), passenger("Raj Sharma", 12, "MALE", "CC-3")));
        Booking b5 = booking("2451234571", p4, t3, LocalDate.now().plusDays(1), TravelClass.AC1, 1, 2200.0 * 3.5, BookingStatus.CANCELLED,
                List.of(passenger("Sneha Iyer", 27, "FEMALE", "AC1-1")));
        b5.setCancelledAt(java.time.LocalDateTime.now());
        bookingRepository.saveAll(List.of(b1, b2, b3, b4, b5));

        // ---- Notifications ----
        notificationRepository.saveAll(List.of(
                notification(p1, "Booking Successful", "Your ticket is confirmed. PNR: 2451234567 for Mumbai Rajdhani."),
                notification(p2, "Booking Successful", "Your ticket is confirmed. PNR: 2451234568 for Coromandel Express."),
                notification(p3, "Trip Completed", "Your journey on Howrah Rajdhani is complete. Thank you!"),
                notification(p1, "Booking Successful", "Your ticket is confirmed. PNR: 2451234570 for Kerala Express."),
                notification(p4, "Ticket Cancelled", "Your ticket with PNR 2451234571 has been cancelled.")
        ));

        log.info("Sample data seeding complete. Admin login: admin / admin123");
    }

    private User createUser(String username, String fullName, String email, String phone,
                            String rawPassword, String gender, Set<Role> roles) {
        return User.builder()
                .username(username)
                .fullName(fullName)
                .email(email)
                .phone(phone)
                .gender(gender)
                .password(passwordEncoder.encode(rawPassword))
                .active(true)
                .roles(new HashSet<>(roles))
                .build();
    }

    private Station station(String code, String name, String city, String state, int platforms) {
        return Station.builder().code(code).name(name).city(city).state(state).platformCount(platforms).build();
    }

    private Train train(String number, String name, TrainType type, Station src, Station dst,
                        int total, int available, double fare, String runningDays) {
        return Train.builder()
                .trainNumber(number).name(name).type(type).source(src).destination(dst)
                .totalSeats(total).availableSeats(available).fare(fare)
                .runningDays(runningDays).status(TrainStatus.ACTIVE).build();
    }

    private Route route(Train train, Station src, Station dst, double distance, String intermediate) {
        return Route.builder()
                .train(train).source(src).destination(dst)
                .distanceKm(distance).intermediateStations(intermediate).build();
    }

    private Schedule schedule(Train train, LocalTime dep, LocalTime arr, int platform, String runningDays) {
        long minutes = java.time.Duration.between(dep, arr).toMinutes();
        if (minutes < 0) minutes += 24 * 60;
        String duration = String.format("%02dh %02dm", minutes / 60, minutes % 60);
        return Schedule.builder()
                .train(train).departureTime(dep).arrivalTime(arr)
                .platform(platform).runningDays(runningDays).duration(duration).build();
    }

    private Booking booking(String pnr, User user, Train train, LocalDate date, TravelClass tc,
                            int seats, double fare, BookingStatus status, List<BookingPassenger> passengers) {
        Booking booking = Booking.builder()
                .pnr(pnr).user(user).train(train).journeyDate(date).travelClass(tc)
                .seatCount(seats).totalFare(fare).status(status).passengers(new ArrayList<>()).build();
        for (BookingPassenger p : passengers) {
            p.setBooking(booking);
            booking.getPassengers().add(p);
        }
        return booking;
    }

    private BookingPassenger passenger(String name, int age, String gender, String seat) {
        return BookingPassenger.builder().name(name).age(age).gender(gender).seatNumber(seat).build();
    }

    private Notification notification(User user, String title, String message) {
        return Notification.builder().user(user).title(title).message(message).readFlag(false).build();
    }
}
