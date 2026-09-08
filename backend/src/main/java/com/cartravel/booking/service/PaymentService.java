package com.cartravel.booking.service;

import com.cartravel.booking.dto.payment.PaymentRequestDTO;
import com.cartravel.booking.dto.payment.PaymentResponseDTO;
import com.cartravel.booking.entity.Booking;
import com.cartravel.booking.entity.Payment;
import com.cartravel.booking.enums.PaymentStatus;
import com.cartravel.booking.exception.BookingNotFoundException;
import com.cartravel.booking.exception.PaymentNotFoundException;
import com.cartravel.booking.repository.BookingRepository;
import com.cartravel.booking.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentService(PaymentRepository paymentRepository, BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository; this.bookingRepository = bookingRepository;
    }

    public List<PaymentResponseDTO> getAllPayments() { return paymentRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList()); }
    public PaymentResponseDTO getPaymentById(Long id) { return mapToDTO(paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException("Payment not found: " + id))); }

    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO req) {
        Booking b = bookingRepository.findById(req.getBookingId()).orElseThrow(() -> new BookingNotFoundException("Booking not found: " + req.getBookingId()));
        String txId = req.getTransactionId() != null && !req.getTransactionId().trim().isEmpty() ? req.getTransactionId().trim() : "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Payment p = Payment.builder().transactionId(txId).booking(b).paymentMethod(req.getPaymentMethod()).amount(req.getAmount()).paymentStatus(req.getPaymentStatus() != null ? req.getPaymentStatus() : PaymentStatus.SUCCESSFUL).build();
        return mapToDTO(paymentRepository.save(p));
    }

    public PaymentResponseDTO mapToDTO(Payment p) {
        return new PaymentResponseDTO(p.getId(), p.getTransactionId(), p.getBooking() != null ? p.getBooking().getId() : null, p.getBooking() != null ? p.getBooking().getBookingNumber() : null, p.getPaymentMethod() != null ? p.getPaymentMethod().name() : "UPI", p.getAmount(), p.getPaymentStatus() != null ? p.getPaymentStatus().name() : "SUCCESSFUL", p.getPaymentDate());
    }
}