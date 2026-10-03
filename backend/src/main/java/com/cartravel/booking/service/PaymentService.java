package com.cartravel.booking.service;

import com.cartravel.booking.dto.payment.PaymentRequestDTO;
import com.cartravel.booking.dto.payment.PaymentResponseDTO;
import com.cartravel.booking.entity.Booking;
import com.cartravel.booking.entity.Payment;
import com.cartravel.booking.entity.User;
import com.cartravel.booking.enums.PaymentStatus;
import com.cartravel.booking.enums.UserRole;
import com.cartravel.booking.exception.BookingNotFoundException;
import com.cartravel.booking.exception.PaymentNotFoundException;
import com.cartravel.booking.exception.UnauthorizedException;
import com.cartravel.booking.repository.BookingRepository;
import com.cartravel.booking.repository.PaymentRepository;
import com.cartravel.booking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          BookingRepository bookingRepository,
                          UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    /** ADMIN ONLY — enforcement at controller level via @PreAuthorize */
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * FIX BUG-007: Enforce ownership — user can only view payments for their own bookings.
     * Admins can view any payment.
     */
    public PaymentResponseDTO getPaymentById(Long id, String callerEmail) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found: " + id));
        User caller = userRepository.findByEmail(callerEmail)
                .orElseThrow(() -> new UnauthorizedException("Session invalid"));
        Booking booking = payment.getBooking();
        if (booking != null && !booking.getUser().getId().equals(caller.getId())
                && caller.getRole() != UserRole.ROLE_ADMIN) {
            throw new UnauthorizedException("Access denied: this payment does not belong to you.");
        }
        return mapToDTO(payment);
    }

    /**
     * FIX BUG-008: Enforce booking ownership before allowing payment creation.
     * FIX BUG-009: Remove client-controlled paymentStatus — always set to INITIATED.
     *              Status should only be updated by a real payment gateway callback (not implemented).
     */
    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO req, String callerEmail) {
        Booking booking = bookingRepository.findById(req.getBookingId())
                .orElseThrow(() -> new BookingNotFoundException("Booking not found: " + req.getBookingId()));
        User caller = userRepository.findByEmail(callerEmail)
                .orElseThrow(() -> new UnauthorizedException("Session invalid"));

        // Enforce ownership: only the booking owner or ADMIN may create a payment
        if (!booking.getUser().getId().equals(caller.getId()) && caller.getRole() != UserRole.ROLE_ADMIN) {
            throw new UnauthorizedException("Access denied: you can only pay for your own bookings.");
        }

        // Generate transaction ID server-side if not provided
        String txId = (req.getTransactionId() != null && !req.getTransactionId().trim().isEmpty())
                ? req.getTransactionId().trim()
                : "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // FIX BUG-009: Always set status to INITIATED — never trust client-provided status.
        // In a real payment gateway integration, status would be updated via webhook/callback.
        Payment p = Payment.builder()
                .transactionId(txId)
                .booking(booking)
                .paymentMethod(req.getPaymentMethod())
                .amount(req.getAmount())
                .paymentStatus(PaymentStatus.INITIATED)
                .build();

        return mapToDTO(paymentRepository.save(p));
    }

    public PaymentResponseDTO mapToDTO(Payment p) {
        return new PaymentResponseDTO(
                p.getId(),
                p.getTransactionId(),
                p.getBooking() != null ? p.getBooking().getId() : null,
                p.getBooking() != null ? p.getBooking().getBookingNumber() : null,
                p.getPaymentMethod() != null ? p.getPaymentMethod().name() : "UPI",
                p.getAmount(),
                p.getPaymentStatus() != null ? p.getPaymentStatus().name() : "INITIATED",
                p.getPaymentDate()
        );
    }
}