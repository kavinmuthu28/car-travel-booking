package com.cartravel.booking.service;

import com.cartravel.booking.dto.booking.FareResponseDTO;
import com.cartravel.booking.enums.TripType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FareCalculationService Unit Tests")
class FareCalculationServiceTest {

    private FareCalculationService fareCalculationService;

    @BeforeEach
    void setUp() {
        fareCalculationService = new FareCalculationService();
        ReflectionTestUtils.setField(fareCalculationService, "oneWayMultiplier", new BigDecimal("1.0"));
        ReflectionTestUtils.setField(fareCalculationService, "roundTripMultiplier", new BigDecimal("2.0"));
        ReflectionTestUtils.setField(fareCalculationService, "multiDayMultiplier", new BigDecimal("2.0"));
        ReflectionTestUtils.setField(fareCalculationService, "baseDriverAllowance", new BigDecimal("500.00"));
    }

    @Test
    @DisplayName("Calculate Total Fare for One Way trip")
    void testCalculateTotalFare_OneWay() {
        BigDecimal distance = new BigDecimal("100.00");
        BigDecimal pricePerKm = new BigDecimal("15.00");
        // base = 100 * 1.0 * 15 = 1500; total = 1500 + 500 = 2000.00
        BigDecimal fare = fareCalculationService.calculateTotalFare(distance, pricePerKm, TripType.ONE_WAY);
        assertEquals(new BigDecimal("2000.00"), fare);
    }

    @Test
    @DisplayName("Calculate Total Fare for Round Trip")
    void testCalculateTotalFare_RoundTrip() {
        BigDecimal distance = new BigDecimal("100.00");
        BigDecimal pricePerKm = new BigDecimal("15.00");
        // base = 100 * 2.0 * 15 = 3000; total = 3000 + 500 = 3500.00
        BigDecimal fare = fareCalculationService.calculateTotalFare(distance, pricePerKm, TripType.ROUND_TRIP);
        assertEquals(new BigDecimal("3500.00"), fare);
    }

    @Test
    @DisplayName("Estimate Fare DTO response contains correct details")
    void testEstimateFare() {
        BigDecimal distance = new BigDecimal("50.00");
        BigDecimal pricePerKm = new BigDecimal("12.00");
        FareResponseDTO dto = fareCalculationService.estimateFare(distance, pricePerKm, TripType.ONE_WAY);

        assertNotNull(dto);
        assertEquals(distance, dto.getDistanceKm());
        assertEquals(pricePerKm, dto.getPricePerKm());
        assertEquals(new BigDecimal("1.0"), dto.getMultiplier());
        assertEquals(new BigDecimal("500.00"), dto.getDriverAllowance());
        assertEquals(new BigDecimal("600.00"), dto.getBaseFare());
        assertEquals(new BigDecimal("1100.00"), dto.getTotalEstimatedFare());
    }

    @Test
    @DisplayName("Null trip type defaults safely to one-way multiplier")
    void testNullTripTypeFallback() {
        BigDecimal multiplier = fareCalculationService.getTripMultiplier(null);
        assertEquals(new BigDecimal("1.0"), multiplier);
    }
}
