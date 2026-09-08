package com.cartravel.booking.enums;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentMethodConverter implements AttributeConverter<PaymentMethod, String> {
    @Override public String convertToDatabaseColumn(PaymentMethod a) { return a == null ? null : a.name().toLowerCase(); }
    @Override public PaymentMethod convertToEntityAttribute(String d) {
        if (d == null || d.trim().isEmpty()) return PaymentMethod.UPI;
        try { return PaymentMethod.valueOf(d.trim().toUpperCase()); } catch (Exception e) { return PaymentMethod.UPI; }
    }
}