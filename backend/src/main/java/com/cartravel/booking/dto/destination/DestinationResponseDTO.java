package com.cartravel.booking.dto.destination;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class DestinationResponseDTO {
    private Long id; private String fromCity; private String toCity; private BigDecimal distanceKm; private String estimatedDuration; private String imageUrl; private String description; private LocalDateTime createdAt;
    public DestinationResponseDTO() {}
    public DestinationResponseDTO(Long id, String fromCity, String toCity, BigDecimal distanceKm, String estimatedDuration, String imageUrl, String description, LocalDateTime createdAt) {
        this.id = id; this.fromCity = fromCity; this.toCity = toCity; this.distanceKm = distanceKm; this.estimatedDuration = estimatedDuration; this.imageUrl = imageUrl; this.description = description; this.createdAt = createdAt;
    }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getFromCity() { return fromCity; } public void setFromCity(String fromCity) { this.fromCity = fromCity; }
    public String getToCity() { return toCity; } public void setToCity(String toCity) { this.toCity = toCity; }
    public BigDecimal getDistanceKm() { return distanceKm; } public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }
    public String getEstimatedDuration() { return estimatedDuration; } public void setEstimatedDuration(String estimatedDuration) { this.estimatedDuration = estimatedDuration; }
    public String getImageUrl() { return imageUrl; } public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}