package com.cartravel.booking.dto.destination;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
public class DestinationRequestDTO {
    @NotBlank(message = "From city is required") private String fromCity;
    @NotBlank(message = "To city is required") private String toCity;
    @NotNull(message = "Distance is required") @DecimalMin(value = "0.0", inclusive = false) private BigDecimal distanceKm;
    @NotBlank(message = "Estimated duration is required") private String estimatedDuration;
    private String imageUrl;
    private String description;
    public DestinationRequestDTO() {}
    public String getFromCity() { return fromCity; } public void setFromCity(String fromCity) { this.fromCity = fromCity; }
    public String getToCity() { return toCity; } public void setToCity(String toCity) { this.toCity = toCity; }
    public BigDecimal getDistanceKm() { return distanceKm; } public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }
    public String getEstimatedDuration() { return estimatedDuration; } public void setEstimatedDuration(String estimatedDuration) { this.estimatedDuration = estimatedDuration; }
    public String getImageUrl() { return imageUrl; } public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
}