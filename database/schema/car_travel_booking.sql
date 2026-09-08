CREATE DATABASE IF NOT EXISTS car_travel_booking CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE car_travel_booking;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS destinations;
DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    role ENUM('role_admin', 'role_customer') NOT NULL DEFAULT 'role_customer',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE cars (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    model_year INT NOT NULL,
    seating_capacity INT NOT NULL,
    fuel_type ENUM('petrol', 'diesel', 'cng', 'electric', 'hybrid') NOT NULL DEFAULT 'petrol',
    transmission ENUM('manual', 'automatic') NOT NULL DEFAULT 'manual',
    price_per_km DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(500) NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE destinations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    from_city VARCHAR(100) NOT NULL,
    to_city VARCHAR(100) NOT NULL,
    distance_km DECIMAL(10, 2) NOT NULL,
    estimated_duration VARCHAR(50) NOT NULL,
    image_url VARCHAR(500) NULL,
    description TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_dest_route (from_city, to_city)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_number VARCHAR(50) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    car_id BIGINT NOT NULL,
    destination_id BIGINT NULL,
    pickup_date DATE NOT NULL,
    pickup_time TIME NOT NULL,
    pickup_address VARCHAR(255) NOT NULL,
    dropoff_address VARCHAR(255) NOT NULL,
    trip_type ENUM('one_way', 'round_trip', 'local_hourly') NOT NULL DEFAULT 'one_way',
    total_distance_km DECIMAL(10, 2) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    booking_status ENUM('pending', 'confirmed', 'in_progress', 'completed', 'cancelled') NOT NULL DEFAULT 'pending',
    special_requests TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_bookings_car FOREIGN KEY (car_id) REFERENCES cars(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_bookings_dest FOREIGN KEY (destination_id) REFERENCES destinations(id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id VARCHAR(100) NOT NULL UNIQUE,
    booking_id BIGINT NOT NULL,
    payment_method ENUM('upi', 'credit_card', 'debit_card', 'net_banking', 'cash') NOT NULL DEFAULT 'upi',
    amount DECIMAL(10, 2) NOT NULL,
    payment_status ENUM('initiated', 'successful', 'failed', 'refunded') NOT NULL DEFAULT 'initiated',
    payment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payments_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;