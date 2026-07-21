-- ==========================================================
-- RailBook Pro - Sample Data
-- ==========================================================
-- Run AFTER schema.sql on a fresh database.
-- NOTE: When running the Spring Boot backend with an EMPTY database,
-- the DataSeeder class auto-inserts equivalent sample data on startup,
-- so you do NOT need to run this script manually in that case.
--
-- Passwords (BCrypt hashed):
--   admin  -> admin123
--   others -> password123
-- ==========================================================

-- ---------- Roles ----------
INSERT INTO roles (id, name) VALUES
    (1, 'ADMIN'),
    (2, 'PASSENGER');

-- ---------- Users ----------
-- BCrypt hash for 'admin123'
INSERT INTO users (id, username, full_name, email, phone, password, gender, active, created_at) VALUES
    (1, 'admin',  'System Administrator', 'admin@railbookpro.com', '9000000000', '$2a$10$FFEhMb2xCotLBQYtZEK3VurDEnzKSq5JbYfuTC2KC5PzSxWafieMO', 'MALE',   1, NOW()),
    (2, 'rahul',  'Rahul Sharma',         'rahul@example.com',     '9811111111', '$2a$10$.XCDmI5Zn2CMfumMynblMuvO/NEv504arJ/ecexwKCfES23v7qt7q', 'MALE',   1, NOW()),
    (3, 'priya',  'Priya Patel',          'priya@example.com',     '9822222222', '$2a$10$.XCDmI5Zn2CMfumMynblMuvO/NEv504arJ/ecexwKCfES23v7qt7q', 'FEMALE', 1, NOW()),
    (4, 'arjun',  'Arjun Reddy',          'arjun@example.com',     '9833333333', '$2a$10$.XCDmI5Zn2CMfumMynblMuvO/NEv504arJ/ecexwKCfES23v7qt7q', 'MALE',   1, NOW()),
    (5, 'sneha',  'Sneha Iyer',           'sneha@example.com',     '9844444444', '$2a$10$.XCDmI5Zn2CMfumMynblMuvO/NEv504arJ/ecexwKCfES23v7qt7q', 'FEMALE', 1, NOW());

-- ---------- User Roles ----------
INSERT INTO user_roles (user_id, role_id) VALUES
    (1, 1),  -- admin  -> ADMIN
    (2, 2),  -- rahul  -> PASSENGER
    (3, 2),
    (4, 2),
    (5, 2);

-- ---------- Stations ----------
INSERT INTO stations (id, code, name, city, state, platform_count) VALUES
    (1, 'BBS',  'Bhubaneswar',      'Bhubaneswar', 'Odisha',        6),
    (2, 'HWH',  'Howrah Junction',  'Howrah',      'West Bengal',   23),
    (3, 'NDLS', 'New Delhi',        'New Delhi',   'Delhi',         16),
    (4, 'MAS',  'Chennai Central',  'Chennai',     'Tamil Nadu',    12),
    (5, 'CSMT', 'Mumbai CSMT',      'Mumbai',      'Maharashtra',   18),
    (6, 'SBC',  'KSR Bengaluru',    'Bengaluru',   'Karnataka',     10);

-- ---------- Trains ----------
INSERT INTO trains (id, train_number, name, type, source_station_id, destination_station_id, total_seats, available_seats, fare, running_days, status) VALUES
    (1, '22952', 'Mumbai Rajdhani',       'RAJDHANI',  5, 3, 900,  900,  2500.0, 'MON,TUE,WED,THU,FRI,SAT,SUN', 'ACTIVE'),
    (2, '12841', 'Coromandel Express',    'SUPERFAST', 2, 4, 1200, 1200, 1200.0, 'MON,TUE,WED,THU,FRI,SAT,SUN', 'ACTIVE'),
    (3, '20817', 'Bhubaneswar Rajdhani',  'RAJDHANI',  1, 3, 800,  800,  2200.0, 'MON,WED,FRI,SUN',             'ACTIVE'),
    (4, '12301', 'Howrah Rajdhani',       'RAJDHANI',  2, 3, 850,  850,  2400.0, 'MON,TUE,WED,THU,FRI,SAT,SUN', 'ACTIVE'),
    (5, '12626', 'Kerala Express',        'SUPERFAST', 3, 4, 1400, 1400, 1500.0, 'MON,TUE,WED,THU,FRI,SAT,SUN', 'ACTIVE');

-- ---------- Routes ----------
INSERT INTO routes (id, train_id, source_station_id, destination_station_id, distance_km, intermediate_stations) VALUES
    (1, 1, 5, 3, 1385.0, 'Vadodara, Kota'),
    (2, 2, 2, 4, 1660.0, 'Bhubaneswar, Vijayawada'),
    (3, 3, 1, 3, 1748.0, 'Kharagpur, Kanpur'),
    (4, 4, 2, 3, 1451.0, 'Dhanbad, Kanpur'),
    (5, 5, 3, 4, 2135.0, 'Bhopal, Nagpur, Vijayawada');

-- ---------- Schedules ----------
INSERT INTO schedules (id, train_id, departure_time, arrival_time, duration, platform, running_days) VALUES
    (1, 1, '17:00:00', '08:35:00', '15h 35m', 5,  'DAILY'),
    (2, 2, '14:50:00', '17:00:00', '02h 10m', 12, 'DAILY'),
    (3, 3, '10:15:00', '10:10:00', '23h 55m', 3,  'MON,WED,FRI,SUN'),
    (4, 4, '16:50:00', '10:00:00', '17h 10m', 8,  'DAILY'),
    (5, 5, '11:25:00', '20:40:00', '09h 15m', 4,  'DAILY');

-- ---------- Bookings ----------
INSERT INTO bookings (id, pnr, user_id, train_id, journey_date, travel_class, seat_count, total_fare, status, booked_at, cancelled_at) VALUES
    (1, '2451234567', 2, 1, DATE_ADD(CURDATE(), INTERVAL 5 DAY),  'AC2', 2, 11000.0, 'CONFIRMED', NOW(), NULL),
    (2, '2451234568', 3, 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY),  'SL',  1, 1200.0,  'CONFIRMED', NOW(), NULL),
    (3, '2451234569', 4, 4, DATE_SUB(CURDATE(), INTERVAL 3 DAY),  'AC3', 1, 3840.0,  'COMPLETED', NOW(), NULL),
    (4, '2451234570', 2, 5, DATE_ADD(CURDATE(), INTERVAL 10 DAY), 'CC',  3, 5850.0,  'CONFIRMED', NOW(), NULL),
    (5, '2451234571', 5, 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY),  'AC1', 1, 7700.0,  'CANCELLED', NOW(), NOW());

-- ---------- Booking Passengers ----------
INSERT INTO booking_passengers (id, booking_id, name, age, gender, seat_number) VALUES
    (1, 1, 'Rahul Sharma', 30, 'MALE',   'AC2-1'),
    (2, 1, 'Anita Sharma', 28, 'FEMALE', 'AC2-2'),
    (3, 2, 'Priya Patel',  25, 'FEMALE', 'SL-1'),
    (4, 3, 'Arjun Reddy',  35, 'MALE',   'AC3-1'),
    (5, 4, 'Rahul Sharma', 30, 'MALE',   'CC-1'),
    (6, 4, 'Meena Sharma', 55, 'FEMALE', 'CC-2'),
    (7, 4, 'Raj Sharma',   12, 'MALE',   'CC-3'),
    (8, 5, 'Sneha Iyer',   27, 'FEMALE', 'AC1-1');

-- ---------- Notifications ----------
INSERT INTO notifications (id, user_id, title, message, read_flag, created_at) VALUES
    (1, 2, 'Booking Successful', 'Your ticket is confirmed. PNR: 2451234567 for Mumbai Rajdhani.', 0, NOW()),
    (2, 3, 'Booking Successful', 'Your ticket is confirmed. PNR: 2451234568 for Coromandel Express.', 0, NOW()),
    (3, 4, 'Trip Completed',     'Your journey on Howrah Rajdhani is complete. Thank you!', 1, NOW()),
    (4, 2, 'Booking Successful', 'Your ticket is confirmed. PNR: 2451234570 for Kerala Express.', 0, NOW()),
    (5, 5, 'Ticket Cancelled',   'Your ticket with PNR 2451234571 has been cancelled.', 0, NOW());

-- ---------- Audit Logs ----------
INSERT INTO audit_logs (id, username, action, details, timestamp) VALUES
    (1, 'admin', 'LOGIN',       'User logged in', NOW()),
    (2, 'rahul', 'BOOK_TICKET', 'PNR 2451234567', NOW()),
    (3, 'priya', 'BOOK_TICKET', 'PNR 2451234568', NOW()),
    (4, 'rahul', 'BOOK_TICKET', 'PNR 2451234570', NOW()),
    (5, 'sneha', 'CANCEL_TICKET','PNR 2451234571', NOW());
