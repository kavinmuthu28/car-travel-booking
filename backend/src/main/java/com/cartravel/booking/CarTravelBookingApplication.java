package com.cartravel.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Entry Point for Car Travel Booking Backend Application.
 * 
 * Powered by Spring Boot 3 & Spring Data JPA.
 */
@SpringBootApplication
public class CarTravelBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarTravelBookingApplication.class, args);
        System.out.println("Car Travel Booking Application started successfully!");
    }
}
