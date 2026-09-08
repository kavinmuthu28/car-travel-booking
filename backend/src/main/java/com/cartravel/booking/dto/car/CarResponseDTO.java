package com.cartravel.booking.dto.car;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class CarResponseDTO {
    private Long id; private String name; private String brand; private Integer modelYear; private Integer seatingCapacity; private String fuelType; private String transmission; private BigDecimal pricePerKm; private String imageUrl; private Boolean isAvailable; private LocalDateTime createdAt;
    public CarResponseDTO() {}
    public CarResponseDTO(Long id, String name, String brand, Integer modelYear, Integer seatingCapacity, String fuelType, String transmission, BigDecimal pricePerKm, String imageUrl, Boolean isAvailable, LocalDateTime createdAt) {
        this.id = id; this.name = name; this.brand = brand; this.modelYear = modelYear; this.seatingCapacity = seatingCapacity; this.fuelType = fuelType; this.transmission = transmission; this.pricePerKm = pricePerKm; this.imageUrl = imageUrl; this.isAvailable = isAvailable; this.createdAt = createdAt;
    }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; } public void setBrand(String brand) { this.brand = brand; }
    public Integer getModelYear() { return modelYear; } public void setModelYear(Integer modelYear) { this.modelYear = modelYear; }
    public Integer getSeatingCapacity() { return seatingCapacity; } public void setSeatingCapacity(Integer seatingCapacity) { this.seatingCapacity = seatingCapacity; }
    public String getFuelType() { return fuelType; } public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    public String getTransmission() { return transmission; } public void setTransmission(String transmission) { this.transmission = transmission; }
    public BigDecimal getPricePerKm() { return pricePerKm; } public void setPricePerKm(BigDecimal pricePerKm) { this.pricePerKm = pricePerKm; }
    public String getImageUrl() { return imageUrl; } public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Boolean getIsAvailable() { return isAvailable; } public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}