package com.cartravel.booking.dto.payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class PaymentResponseDTO {
    private Long id; private String transactionId; private Long bookingId; private String bookingNumber; private String paymentMethod; private BigDecimal amount; private String paymentStatus; private LocalDateTime paymentDate;
    public PaymentResponseDTO() {}
    public PaymentResponseDTO(Long id, String transactionId, Long bookingId, String bookingNumber, String paymentMethod, BigDecimal amount, String paymentStatus, LocalDateTime paymentDate) {
        this.id = id; this.transactionId = transactionId; this.bookingId = bookingId; this.bookingNumber = bookingNumber; this.paymentMethod = paymentMethod; this.amount = amount; this.paymentStatus = paymentStatus; this.paymentDate = paymentDate;
    }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getTransactionId() { return transactionId; } public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public Long getBookingId() { return bookingId; } public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public String getBookingNumber() { return bookingNumber; } public void setBookingNumber(String bookingNumber) { this.bookingNumber = bookingNumber; }
    public String getPaymentMethod() { return paymentMethod; } public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public BigDecimal getAmount() { return amount; } public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getPaymentStatus() { return paymentStatus; } public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public LocalDateTime getPaymentDate() { return paymentDate; } public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
}