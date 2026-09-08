package com.cartravel.booking.entity;

import com.cartravel.booking.enums.PaymentMethod;
import com.cartravel.booking.enums.PaymentMethodConverter;
import com.cartravel.booking.enums.PaymentStatus;
import com.cartravel.booking.enums.PaymentStatusConverter;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "transaction_id", nullable = false, unique = true, length = 100)
    private String transactionId;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    @Column(name = "payment_method", nullable = false)
    @Convert(converter = PaymentMethodConverter.class)
    private PaymentMethod paymentMethod;
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    @Column(name = "payment_status", nullable = false)
    @Convert(converter = PaymentStatusConverter.class)
    private PaymentStatus paymentStatus;
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    public Payment() {}
    public Payment(Long id, String transactionId, Booking booking, PaymentMethod paymentMethod, BigDecimal amount, PaymentStatus paymentStatus, LocalDateTime paymentDate) {
        this.id = id; this.transactionId = transactionId; this.booking = booking; this.paymentMethod = paymentMethod; this.amount = amount; this.paymentStatus = paymentStatus; this.paymentDate = paymentDate;
    }
    @PrePersist protected void onCreate() {
        if (this.paymentDate == null) this.paymentDate = LocalDateTime.now();
        if (this.paymentStatus == null) this.paymentStatus = PaymentStatus.INITIATED;
    }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getTransactionId() { return transactionId; } public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public Booking getBooking() { return booking; } public void setBooking(Booking booking) { this.booking = booking; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; } public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public BigDecimal getAmount() { return amount; } public void setAmount(BigDecimal amount) { this.amount = amount; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; } public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public LocalDateTime getPaymentDate() { return paymentDate; } public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public static PaymentBuilder builder() { return new PaymentBuilder(); }
    public static class PaymentBuilder {
        private Long id; private String transactionId; private Booking booking; private PaymentMethod paymentMethod; private BigDecimal amount; private PaymentStatus paymentStatus; private LocalDateTime paymentDate;
        public PaymentBuilder id(Long id) { this.id = id; return this; }
        public PaymentBuilder transactionId(String transactionId) { this.transactionId = transactionId; return this; }
        public PaymentBuilder booking(Booking booking) { this.booking = booking; return this; }
        public PaymentBuilder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public PaymentBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public PaymentBuilder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public PaymentBuilder paymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; return this; }
        public Payment build() { return new Payment(id, transactionId, booking, paymentMethod, amount, paymentStatus, paymentDate); }
    }
}