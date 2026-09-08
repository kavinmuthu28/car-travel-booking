package com.cartravel.booking.dto.car;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
public class CarRequestDTO {
    @NotBlank(message = "Car name is required") private String name;
    @NotBlank(message = "Brand is required") private String brand;
    @NotNull(message = "Model year is required") @Min(value = 1990) private Integer modelYear;
    @NotNull(message = "Seating capacity is required") @Min(value = 1) private Integer seatingCapacity;
    @NotBlank(message = "Fuel type is required") private String fuelType;
    @NotBlank(message = "Transmission is required") private String transmission;
    @NotNull(message = "Price per km is required") @DecimalMin(value = "0.0", inclusive = false) private BigDecimal pricePerKm;
    private String imageUrl;
    private Boolean isAvailable = true;
    public CarRequestDTO() {}
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; } public void setBrand(String brand) { this.brand = brand; }
    public Integer getModelYear() { return modelYear; } public void setModelYear(Integer modelYear) { this.modelYear = modelYear; }
    public Integer getSeatingCapacity() { return seatingCapacity; } public void setSeatingCapacity(Integer seatingCapacity) { this.seatingCapacity = seatingCapacity; }
    public String getFuelType() { return fuelType; } public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    public String getTransmission() { return transmission; } public void setTransmission(String transmission) { this.transmission = transmission; }
    public BigDecimal getPricePerKm() { return pricePerKm; } public void setPricePerKm(BigDecimal pricePerKm) { this.pricePerKm = pricePerKm; }
    public String getImageUrl() { return imageUrl; } public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Boolean getIsAvailable() { return isAvailable; } public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
}