package com.cartravel.booking.enums;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<UserRole, String> {
    @Override public String convertToDatabaseColumn(UserRole a) { return a == null ? null : a.name().toLowerCase(); }
    @Override public UserRole convertToEntityAttribute(String d) {
        if (d == null || d.trim().isEmpty()) return null;
        String c = d.trim().toUpperCase();
        return (c.equals("ROLE_ADMIN") || c.equals("ADMIN")) ? UserRole.ROLE_ADMIN : UserRole.ROLE_CUSTOMER;
    }
}