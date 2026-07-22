-- Train Ticket Booking System - Database Script
-- For freesqldatabase.com (uses the pre-created database sql12833542)
-- No CREATE DATABASE / USE here: the free host only allows the single provided DB.

DROP TABLE IF EXISTS TicketBookings;

CREATE TABLE TicketBookings (
    booking_id          VARCHAR(20)  NOT NULL PRIMARY KEY,
    passenger_name      VARCHAR(100) NOT NULL,
    age                 INT          NOT NULL,
    gender              VARCHAR(10)  NOT NULL,
    train_number        VARCHAR(20)  NOT NULL,
    train_name          VARCHAR(100) NOT NULL,
    source_station      VARCHAR(100) NOT NULL,
    destination_station VARCHAR(100) NOT NULL,
    journey_date        DATE         NOT NULL,
    coach_type          VARCHAR(20)  NOT NULL,
    seat_number         VARCHAR(10)  NOT NULL
);

INSERT INTO TicketBookings
    (booking_id, passenger_name, age, gender, train_number, train_name,
     source_station, destination_station, journey_date, coach_type, seat_number)
VALUES
    ('BK001', 'Rahul Sharma',   28, 'Male',   '12841', 'Coromandel Express', 'Howrah',    'Chennai Central', '2026-08-10', 'Sleeper', 'S4-32'),
    ('BK002', 'Priya Nair',     34, 'Female', '20817', 'Rajdhani Express',   'Bhubaneswar', 'New Delhi',     '2026-08-12', '3A',      'B2-15'),
    ('BK003', 'Amit Verma',     45, 'Male',   '12626', 'Kerala Express',     'New Delhi', 'Thiruvananthapuram', '2026-08-15', '2A',   'A1-08'),
    ('BK004', 'Sneha Iyer',     22, 'Female', '12301', 'Howrah Rajdhani',    'New Delhi', 'Howrah',          '2026-08-18', '1A',      'H1-02'),
    ('BK005', 'Vikram Singh',   39, 'Male',   '22952', 'Mumbai Rajdhani',    'Bandra Terminus', 'New Delhi', '2026-08-20', '3A',      'B5-45');

SELECT * FROM TicketBookings;
