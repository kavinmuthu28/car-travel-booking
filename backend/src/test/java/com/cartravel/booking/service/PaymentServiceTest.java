package com.cartravel.booking.service;

import com.cartravel.booking.dto.payment.PaymentRequestDTO;
import com.cartravel.booking.dto.payment.PaymentResponseDTO;
import com.cartravel.booking.entity.Booking;
import com.cartravel.booking.entity.Payment;
import com.cartravel.booking.entity.User;
import com.cartravel.booking.enums.PaymentMethod;
import com.cartravel.booking.enums.PaymentStatus;
import com.cartravel.booking.enums.UserRole;
import com.cartravel.booking.exception.UnauthorizedException;
import com.cartravel.booking.repository.BookingRepository;
import com.cartravel.booking.repository.PaymentRepository;
import com.cartravel.booking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentService Unit & Security Tests")
class PaymentServiceTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private BookingRepository bookingRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private PaymentService paymentService;

    private User customerA;
    private User customerB;
    private Booking bookingA;

    @BeforeEach
    void setUp() {
        customerA = User.builder()
                .id(1L)
                .name("Customer A")
                .email("custA@example.com")
                .phone("9876543210")
                .role(UserRole.ROLE_CUSTOMER)
                .build();

        customerB = User.builder()
                .id(2L)
                .name("Customer B")
                .email("custB@example.com")
                .phone("9876543211")
                .role(UserRole.ROLE_CUSTOMER)
                .build();

        bookingA = Booking.builder()
                .id(10L)
                .bookingNumber("KM-261003-PAY1")
                .user(customerA)
                .totalAmount(new BigDecimal("2500.00"))
                .build();
    }

    @Test
    @DisplayName("BUG-008: Reject payment processing when booking belongs to another user")
    void testProcessPayment_CustomerBPayForCustomerABooking_ThrowsUnauthorized() {
        PaymentRequestDTO req = new PaymentRequestDTO();
        req.setBookingId(10L);
        req.setPaymentMethod(PaymentMethod.UPI);
        req.setAmount(new BigDecimal("2500.00"));

        when(bookingRepository.findById(10L)).thenReturn(Optional.of(bookingA));
        when(userRepository.findByEmail("custB@example.com")).thenReturn(Optional.of(customerB));

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () ->
                paymentService.processPayment(req, "custB@example.com"));

        assertTrue(ex.getMessage().contains("Access denied"));
        verify(paymentRepository, never()).save(any());
    }

    @Test
    @DisplayName("BUG-009: Payment status is forced to INITIATED by backend, ignoring client payload")
    void testProcessPayment_IgnoresClientPaymentStatus_ForcesInitiated() {
        PaymentRequestDTO req = new PaymentRequestDTO();
        req.setBookingId(10L);
        req.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        req.setAmount(new BigDecimal("2500.00"));
        // Attempting to send SUCCESSFUL status from client
        req.setPaymentStatus(PaymentStatus.SUCCESSFUL);

        when(bookingRepository.findById(10L)).thenReturn(Optional.of(bookingA));
        when(userRepository.findByEmail("custA@example.com")).thenReturn(Optional.of(customerA));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setId(99L);
            return p;
        });

        PaymentResponseDTO response = paymentService.processPayment(req, "custA@example.com");

        assertNotNull(response);
        // Crucial verification: backend forces INITIATED regardless of client payload
        assertEquals("INITIATED", response.getPaymentStatus());
    }

    @Test
    @DisplayName("BUG-007: Customer cannot view another customer's payment record")
    void testGetPaymentById_UnauthorizedCustomer_ThrowsUnauthorized() {
        Payment payment = Payment.builder()
                .id(50L)
                .transactionId("TXN-12345678")
                .booking(bookingA)
                .amount(new BigDecimal("2500.00"))
                .paymentStatus(PaymentStatus.SUCCESSFUL)
                .build();

        when(paymentRepository.findById(50L)).thenReturn(Optional.of(payment));
        when(userRepository.findByEmail("custB@example.com")).thenReturn(Optional.of(customerB));

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () ->
                paymentService.getPaymentById(50L, "custB@example.com"));

        assertTrue(ex.getMessage().contains("Access denied"));
    }
}
