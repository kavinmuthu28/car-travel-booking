package com.cartravel.booking.controller;

import com.cartravel.booking.dto.payment.PaymentRequestDTO;
import com.cartravel.booking.dto.payment.PaymentResponseDTO;
import com.cartravel.booking.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PaymentController — REST endpoints for payment operations.
 *
 * Security model:
 *   GET  /api/payments        → ADMIN ONLY
 *   GET  /api/payments/{id}   → Owner (user whose booking it is) or ADMIN
 *   POST /api/payments        → Authenticated user (must own the booking)
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * FIX BUG-007: Was accessible to any authenticated user.
     * Now restricted to ADMIN only.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    /**
     * FIX BUG-007: Any authenticated user could view any payment record.
     * Now enforces ownership — only the booking owner or ADMIN.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(
            @PathVariable Long id, Authentication a) {
        return ResponseEntity.ok(paymentService.getPaymentById(id, a.getName()));
    }

    /**
     * FIX BUG-008/BUG-009: Was accessible to any authenticated user for any booking.
     * Now enforces booking ownership before allowing payment creation.
     * Also removes client-controlled paymentStatus (always set to INITIATED server-side).
     */
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> processPayment(
            @Valid @RequestBody PaymentRequestDTO req, Authentication a) {
        return new ResponseEntity<>(paymentService.processPayment(req, a.getName()), HttpStatus.CREATED);
    }
}