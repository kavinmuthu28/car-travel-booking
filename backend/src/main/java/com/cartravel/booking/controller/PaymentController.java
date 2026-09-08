package com.cartravel.booking.controller;

import com.cartravel.booking.dto.payment.PaymentRequestDTO;
import com.cartravel.booking.dto.payment.PaymentResponseDTO;
import com.cartravel.booking.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) { this.paymentService = paymentService; }
    @GetMapping public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() { return ResponseEntity.ok(paymentService.getAllPayments()); }
    @GetMapping("/{id}") public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long id) { return ResponseEntity.ok(paymentService.getPaymentById(id)); }
    @PostMapping public ResponseEntity<PaymentResponseDTO> processPayment(@Valid @RequestBody PaymentRequestDTO req) { return new ResponseEntity<>(paymentService.processPayment(req), HttpStatus.CREATED); }
}