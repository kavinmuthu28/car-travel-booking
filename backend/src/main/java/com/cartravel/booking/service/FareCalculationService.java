package com.cartravel.booking.service;

import com.cartravel.booking.dto.booking.FareResponseDTO;
import com.cartravel.booking.enums.TripType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class FareCalculationService {
    @Value("${app.pricing.multiplier.one-way:1.0}") private BigDecimal oneWayMultiplier;
    @Value("${app.pricing.multiplier.round-trip:2.0}") private BigDecimal roundTripMultiplier;
    @Value("${app.pricing.multiplier.multi-day:2.0}") private BigDecimal multiDayMultiplier;
    @Value("${app.pricing.driver-allowance.base:500.00}") private BigDecimal baseDriverAllowance;

    public BigDecimal getTripMultiplier(TripType tripType) {
        if (tripType == null) return oneWayMultiplier;
        return switch (tripType) {
            case ROUND_TRIP -> roundTripMultiplier;
            case MULTI_DAY -> multiDayMultiplier;
            default -> oneWayMultiplier;
        };
    }

    public BigDecimal calculateTotalFare(BigDecimal distanceKm, BigDecimal pricePerKm, TripType tripType) {
        BigDecimal multiplier = getTripMultiplier(tripType);
        BigDecimal base = distanceKm.multiply(multiplier).multiply(pricePerKm);
        return base.add(baseDriverAllowance).setScale(2, RoundingMode.HALF_UP);
    }

    public FareResponseDTO estimateFare(BigDecimal distanceKm, BigDecimal pricePerKm, TripType tripType) {
        BigDecimal multiplier = getTripMultiplier(tripType);
        BigDecimal base = distanceKm.multiply(multiplier).multiply(pricePerKm).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = base.add(baseDriverAllowance).setScale(2, RoundingMode.HALF_UP);
        return new FareResponseDTO(distanceKm, pricePerKm, multiplier, baseDriverAllowance, base, total);
    }
}