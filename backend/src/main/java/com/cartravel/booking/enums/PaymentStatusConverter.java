package com.cartravel.booking.enums;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentStatusConverter implements AttributeConverter<PaymentStatus, String> {
    @Override public String convertToDatabaseColumn(PaymentStatus a) { return a == null ? null : a.name().toLowerCase(); }
    @Override public PaymentStatus convertToEntityAttribute(String d) {
        if (d == null || d.trim().isEmpty()) return PaymentStatus.INITIATED;
        try { return PaymentStatus.valueOf(d.trim().toUpperCase()); } catch (Exception e) { return PaymentStatus.INITIATED; }
    }
}