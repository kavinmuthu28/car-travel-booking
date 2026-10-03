package com.cartravel.booking.enums;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converts TripType enum to/from the lowercase database string representation.
 *
 * NOTE (BUG-011): The original database schema defined the trip_type column
 * with ENUM values: ('one_way', 'round_trip', 'local_hourly').
 * The Java enum was later refactored to use MULTI_DAY instead of LOCAL_HOURLY.
 * Any existing DB rows with 'local_hourly' will be safely mapped to ONE_WAY
 * as a fallback. If you have existing data with 'local_hourly', review those
 * records and update them to 'one_way' or 'multi_day' as appropriate.
 * The DB schema ENUM should also be updated to include 'multi_day'.
 */
@Converter(autoApply = true)
public class TripTypeConverter implements AttributeConverter<TripType, String> {
    @Override
    public String convertToDatabaseColumn(TripType a) {
        return a == null ? null : a.name().toLowerCase();
    }

    @Override
    public TripType convertToEntityAttribute(String d) {
        if (d == null || d.trim().isEmpty()) return TripType.ONE_WAY;
        String c = d.trim().toUpperCase();
        if (c.equals("ROUND_TRIP")) return TripType.ROUND_TRIP;
        if (c.equals("MULTI_DAY")) return TripType.MULTI_DAY;
        // FIX BUG-011: 'local_hourly' exists in DB schema but not in Java enum.
        // Map it to ONE_WAY as the closest safe fallback.
        if (c.equals("LOCAL_HOURLY")) return TripType.ONE_WAY;
        return TripType.ONE_WAY;
    }
}