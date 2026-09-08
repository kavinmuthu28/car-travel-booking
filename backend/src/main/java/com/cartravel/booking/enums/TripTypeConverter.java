package com.cartravel.booking.enums;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TripTypeConverter implements AttributeConverter<TripType, String> {
    @Override public String convertToDatabaseColumn(TripType a) { return a == null ? null : a.name().toLowerCase(); }
    @Override public TripType convertToEntityAttribute(String d) {
        if (d == null || d.trim().isEmpty()) return TripType.ONE_WAY;
        String c = d.trim().toUpperCase();
        if (c.equals("ROUND_TRIP")) return TripType.ROUND_TRIP;
        if (c.equals("MULTI_DAY")) return TripType.MULTI_DAY;
        return TripType.ONE_WAY;
    }
}