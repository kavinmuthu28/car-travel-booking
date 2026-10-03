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

/**
 * BookingController — REST endpoints for booking operations.
 *
 * Security model:
 *   POST   /api/bookings              → Any authenticated user (creates their own booking)
 *   GET    /api/bookings              → ADMIN ONLY (all bookings)
 *   GET    /api/bookings/my           → Authenticated user (their own bookings)
 *   GET    /api/bookings/user/{id}    → ADMIN ONLY (bookings for a specific user)
 *   GET    /api/bookings/{id}         → Authenticated user (own booking) or ADMIN
 *   PUT    /api/bookings/{id}/status  → ADMIN ONLY
 *   PUT    /api/bookings/{id}/cancel  → Authenticated user (own booking) or ADMIN
 *   POST   /api/bookings/estimate-fare → Public (fare estimation)
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final FareCalculationService fareCalculationService;

    public BookingController(BookingService bookingService, FareCalculationService fareCalculationService) {
        this.bookingService = bookingService;
        this.fareCalculationService = fareCalculationService;
    }

    /** Create a booking — authenticated users book for themselves */
    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Valid @RequestBody BookingRequestDTO req, Authentication a) {
        return new ResponseEntity<>(bookingService.createBooking(req, a.getName()), HttpStatus.CREATED);
    }

    /**
     * Get ALL bookings — ADMIN ONLY.
     * FIX BUG-004/BUG-028: Was previously accessible to any authenticated user.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    /**
     * Get the current user's own bookings (self-service endpoint).
     * FIX BUG-002: Replaces the insecure /user/{userId} pattern for customers.
     */
    @GetMapping("/my")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings(Authentication a) {
        return ResponseEntity.ok(bookingService.getBookingsByEmail(a.getName()));
    }

    /**
     * Get bookings for a specific user ID — ADMIN ONLY.
     * FIX BUG-002: Was previously accessible to any authenticated user (IDOR).
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getBookingsByUserId(userId));
    }

    /**
     * Get a single booking by ID.
     * FIX BUG-003: Now enforces ownership — user can only view own booking; admin can view all.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(
            @PathVariable Long id, Authentication a) {
        return ResponseEntity.ok(bookingService.getBookingById(id, a.getName()));
    }

    /** Update booking status — ADMIN ONLY */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingResponseDTO> updateBookingStatus(
            @PathVariable Long id, @Valid @RequestBody BookingUpdateDTO req) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, req));
    }

    /** Cancel a booking — authenticated user (own booking) or admin */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(
            @PathVariable Long id, Authentication a) {
        return ResponseEntity.ok(bookingService.cancelBooking(id, a.getName()));
    }

    /** Estimate fare — public endpoint (no auth required) */
    @PostMapping("/estimate-fare")
    public ResponseEntity<FareResponseDTO> estimateFare(
            @RequestParam BigDecimal distanceKm,
            @RequestParam BigDecimal pricePerKm,
            @RequestParam(defaultValue = "ONE_WAY") TripType tripType) {
        return ResponseEntity.ok(fareCalculationService.estimateFare(distanceKm, pricePerKm, tripType));
    }
}