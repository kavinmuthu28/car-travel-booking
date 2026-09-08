package com.cartravel.booking.repository;
import com.cartravel.booking.entity.Payment;
import com.cartravel.booking.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionId(String transactionId);
    Optional<Payment> findByBookingId(Long bookingId);
    List<Payment> findByPaymentStatusOrderByPaymentDateDesc(PaymentStatus paymentStatus);
}