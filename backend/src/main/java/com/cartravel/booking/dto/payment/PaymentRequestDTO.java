package com.cartravel.booking.dto.payment;
import com.cartravel.booking.enums.PaymentMethod;
import com.cartravel.booking.enums.PaymentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
public class PaymentRequestDTO {
    @NotNull(message = "Booking ID is required") private Long bookingId;
    private String transactionId;
    private PaymentMethod paymentMethod = PaymentMethod.UPI;
    @NotNull(message = "Amount is required") @DecimalMin(value = "0.0") private BigDecimal amount;
    private PaymentStatus paymentStatus = PaymentStatus.SUCCESSFUL;
    public PaymentRequestDTO() {}
    public Long getBookingId() { return bookingId; } public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public String getTransactionId() { return transactionId; } public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; } public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public BigDecimal getAmount() { return amount; } public void setAmount(BigDecimal amount) { this.amount = amount; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; } public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
}