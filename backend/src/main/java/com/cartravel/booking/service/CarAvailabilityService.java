package com.cartravel.booking.service;

import com.cartravel.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class CarAvailabilityService {
    private final BookingRepository bookingRepository;
    public CarAvailabilityService(BookingRepository bookingRepository) { this.bookingRepository = bookingRepository; }
    public boolean isCarAvailableForDate(Long carId, LocalDate date) {
        return !bookingRepository.existsByCarIdAndPickupDateAndNotCancelled(carId, date);
    }
}