-- ==========================================================
-- RailBook Pro - MySQL Schema
-- Compatible with MySQL 5.5+ (no fractional-second precision)
-- ==========================================================
-- NOTE: The Spring Boot backend auto-creates these tables via
-- Hibernate (spring.jpa.hibernate.ddl-auto=update). This script
-- is provided for manual/standalone database setup and reference.
-- ==========================================================

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS booking_passengers;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS audit_logs;
DROP TABLE IF EXISTS schedules;
DROP TABLE IF EXISTS routes;
DROP TABLE IF EXISTS trains;
DROP TABLE IF EXISTS stations;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

SET FOREIGN_KEY_CHECKS = 1;

-- ---------- Roles ----------
CREATE TABLE roles (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    CONSTRAINT uk_roles_name UNIQUE (name)
) ENGINE=InnoDB;

-- ---------- Users ----------
CREATE TABLE users (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(60)  NOT NULL,
    full_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(120) NOT NULL,
    phone      VARCHAR(15)  NOT NULL,
    password   VARCHAR(255) NOT NULL,
    gender     VARCHAR(10),
    active     BIT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email)
) ENGINE=InnoDB;

-- ---------- User <-> Role join ----------
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------- Stations ----------
CREATE TABLE stations (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    code           VARCHAR(10)  NOT NULL,
    name           VARCHAR(100) NOT NULL,
    city           VARCHAR(60)  NOT NULL,
    state          VARCHAR(60)  NOT NULL,
    platform_count INT NOT NULL,
    CONSTRAINT uk_stations_code UNIQUE (code)
) ENGINE=InnoDB;

-- ---------- Trains ----------
CREATE TABLE trains (
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    train_number           VARCHAR(10)  NOT NULL,
    name                   VARCHAR(100) NOT NULL,
    type                   VARCHAR(20)  NOT NULL,
    source_station_id      BIGINT NOT NULL,
    destination_station_id BIGINT NOT NULL,
    total_seats            INT NOT NULL,
    available_seats        INT NOT NULL,
    fare                   DOUBLE NOT NULL,
    running_days           VARCHAR(30) NOT NULL,
    status                 VARCHAR(20) NOT NULL,
    CONSTRAINT uk_trains_number UNIQUE (train_number),
    CONSTRAINT fk_train_source FOREIGN KEY (source_station_id) REFERENCES stations (id),
    CONSTRAINT fk_train_dest   FOREIGN KEY (destination_station_id) REFERENCES stations (id)
) ENGINE=InnoDB;

-- ---------- Routes ----------
CREATE TABLE routes (
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    train_id               BIGINT NOT NULL,
    source_station_id      BIGINT NOT NULL,
    destination_station_id BIGINT NOT NULL,
    distance_km            DOUBLE NOT NULL,
    intermediate_stations  VARCHAR(500),
    CONSTRAINT fk_route_train  FOREIGN KEY (train_id) REFERENCES trains (id) ON DELETE CASCADE,
    CONSTRAINT fk_route_source FOREIGN KEY (source_station_id) REFERENCES stations (id),
    CONSTRAINT fk_route_dest   FOREIGN KEY (destination_station_id) REFERENCES stations (id)
) ENGINE=InnoDB;

-- ---------- Schedules ----------
CREATE TABLE schedules (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    train_id       BIGINT NOT NULL,
    departure_time TIME NOT NULL,
    arrival_time   TIME NOT NULL,
    duration       VARCHAR(20) NOT NULL,
    platform       INT NOT NULL,
    running_days   VARCHAR(30) NOT NULL,
    CONSTRAINT fk_schedule_train FOREIGN KEY (train_id) REFERENCES trains (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------- Bookings ----------
CREATE TABLE bookings (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    pnr          VARCHAR(15) NOT NULL,
    user_id      BIGINT NOT NULL,
    train_id     BIGINT NOT NULL,
    journey_date DATE NOT NULL,
    travel_class VARCHAR(10) NOT NULL,
    seat_count   INT NOT NULL,
    total_fare   DOUBLE NOT NULL,
    status       VARCHAR(20) NOT NULL,
    booked_at    DATETIME NOT NULL,
    cancelled_at DATETIME,
    CONSTRAINT uk_bookings_pnr UNIQUE (pnr),
    CONSTRAINT fk_booking_user  FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_booking_train FOREIGN KEY (train_id) REFERENCES trains (id)
) ENGINE=InnoDB;

-- ---------- Booking Passengers ----------
CREATE TABLE booking_passengers (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id  BIGINT NOT NULL,
    name        VARCHAR(100) NOT NULL,
    age         INT NOT NULL,
    gender      VARCHAR(10) NOT NULL,
    seat_number VARCHAR(10),
    CONSTRAINT fk_bp_booking FOREIGN KEY (booking_id) REFERENCES bookings (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------- Notifications ----------
CREATE TABLE notifications (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    title      VARCHAR(100) NOT NULL,
    message    VARCHAR(500) NOT NULL,
    read_flag  BIT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------- Audit Logs ----------
CREATE TABLE audit_logs (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(60),
    action    VARCHAR(60) NOT NULL,
    details   VARCHAR(500),
    timestamp DATETIME NOT NULL
) ENGINE=InnoDB;
