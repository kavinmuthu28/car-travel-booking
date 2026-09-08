package com.cartravel.booking.dto.booking;
import java.math.BigDecimal;
public class FareResponseDTO {
    private BigDecimal distanceKm; private BigDecimal pricePerKm; private BigDecimal multiplier; private BigDecimal driverAllowance; private BigDecimal baseFare; private BigDecimal totalEstimatedFare;
    public FareResponseDTO() {}
    public FareResponseDTO(BigDecimal distanceKm, BigDecimal pricePerKm, BigDecimal multiplier, BigDecimal driverAllowance, BigDecimal baseFare, BigDecimal totalEstimatedFare) {
        this.distanceKm = distanceKm; this.pricePerKm = pricePerKm; this.multiplier = multiplier; this.driverAllowance = driverAllowance; this.baseFare = baseFare; this.totalEstimatedFare = totalEstimatedFare;
    }
    public BigDecimal getDistanceKm() { return distanceKm; } public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }
    public BigDecimal getPricePerKm() { return pricePerKm; } public void setPricePerKm(BigDecimal pricePerKm) { this.pricePerKm = pricePerKm; }
    public BigDecimal getMultiplier() { return multiplier; } public void setMultiplier(BigDecimal multiplier) { this.multiplier = multiplier; }
    public BigDecimal getDriverAllowance() { return driverAllowance; } public void setDriverAllowance(BigDecimal driverAllowance) { this.driverAllowance = driverAllowance; }
    public BigDecimal getBaseFare() { return baseFare; } public void setBaseFare(BigDecimal baseFare) { this.baseFare = baseFare; }
    public BigDecimal getTotalEstimatedFare() { return totalEstimatedFare; } public void setTotalEstimatedFare(BigDecimal totalEstimatedFare) { this.totalEstimatedFare = totalEstimatedFare; }
}