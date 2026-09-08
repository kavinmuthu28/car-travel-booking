package com.cartravel.booking.enums;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BookingStatusConverter implements AttributeConverter<BookingStatus, String> {
    @Override public String convertToDatabaseColumn(BookingStatus a) { return a == null ? null : a.name().toLowerCase(); }
    @Override public BookingStatus convertToEntityAttribute(String d) {
        if (d == null || d.trim().isEmpty()) return BookingStatus.PENDING;
        try { return BookingStatus.valueOf(d.trim().toUpperCase()); } catch (Exception e) { return BookingStatus.PENDING; }
    }
}