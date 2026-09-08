package com.cartravel.booking.controller;

import com.cartravel.booking.dto.booking.BookingRequestDTO;
import com.cartravel.booking.dto.booking.BookingResponseDTO;
import com.cartravel.booking.dto.booking.BookingUpdateDTO;
import com.cartravel.booking.dto.booking.FareResponseDTO;
import com.cartravel.booking.enums.TripType;
import com.cartravel.booking.service.BookingService;
import com.cartravel.booking.service.FareCalculationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final FareCalculationService fareCalculationService;
    public BookingController(BookingService bookingService, FareCalculationService fareCalculationService) {
        this.bookingService = bookingService; this.fareCalculationService = fareCalculationService;
    }
    @PostMapping public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO req, Authentication a) { return new ResponseEntity<>(bookingService.createBooking(req, a.getName()), HttpStatus.CREATED); }
    @GetMapping public ResponseEntity<List<BookingResponseDTO>> getAllBookings() { return ResponseEntity.ok(bookingService.getAllBookings()); }
    @GetMapping("/user/{userId}") public ResponseEntity<List<BookingResponseDTO>> getBookingsByUserId(@PathVariable Long userId) { return ResponseEntity.ok(bookingService.getBookingsByUserId(userId)); }
    @GetMapping("/{id}") public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) { return ResponseEntity.ok(bookingService.getBookingById(id)); }
    @PutMapping("/{id}/status") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<BookingResponseDTO> updateBookingStatus(@PathVariable Long id, @Valid @RequestBody BookingUpdateDTO req) { return ResponseEntity.ok(bookingService.updateBookingStatus(id, req)); }
    @PutMapping("/{id}/cancel") public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long id, Authentication a) { return ResponseEntity.ok(bookingService.cancelBooking(id, a.getName())); }
    @PostMapping("/estimate-fare") public ResponseEntity<FareResponseDTO> estimateFare(@RequestParam BigDecimal distanceKm, @RequestParam BigDecimal pricePerKm, @RequestParam(defaultValue = "ONE_WAY") TripType tripType) { return ResponseEntity.ok(fareCalculationService.estimateFare(distanceKm, pricePerKm, tripType)); }
}