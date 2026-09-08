package com.cartravel.booking.controller;

import com.cartravel.booking.dto.booking.BookingResponseDTO;
import com.cartravel.booking.dto.car.CarResponseDTO;
import com.cartravel.booking.dto.payment.PaymentResponseDTO;
import com.cartravel.booking.dto.user.UserResponseDTO;
import com.cartravel.booking.service.BookingService;
import com.cartravel.booking.service.CarService;
import com.cartravel.booking.service.PaymentService;
import com.cartravel.booking.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final CarService carService;
    private final BookingService bookingService;
    private final UserService userService;
    private final PaymentService paymentService;

    public AdminController(CarService carService, BookingService bookingService, UserService userService, PaymentService paymentService) {
        this.carService = carService; this.bookingService = bookingService; this.userService = userService; this.paymentService = paymentService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAdminStats() {
        List<CarResponseDTO> cars = carService.getAllCars();
        List<BookingResponseDTO> bookings = bookingService.getAllBookings();
        List<UserResponseDTO> users = userService.getAllUsers();
        List<PaymentResponseDTO> payments = paymentService.getAllPayments();

        long availableCars = cars.stream().filter(CarResponseDTO::getIsAvailable).count();
        double totalRevenue = payments.stream()
                .filter(p -> "SUCCESSFUL".equalsIgnoreCase(p.getPaymentStatus()))
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount().doubleValue() : 0.0)
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCars", cars.size());
        stats.put("availableCars", availableCars);
        stats.put("totalBookings", bookings.size());
        stats.put("totalUsers", users.size());
        stats.put("totalRevenue", totalRevenue);
        return ResponseEntity.ok(stats);
    }
}