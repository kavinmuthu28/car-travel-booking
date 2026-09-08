package com.cartravel.booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "brand", nullable = false, length = 100)
    private String brand;
    @Column(name = "model_year", nullable = false)
    private Integer modelYear;
    @Column(name = "seating_capacity", nullable = false)
    private Integer seatingCapacity;
    @Column(name = "fuel_type", nullable = false, length = 50)
    private String fuelType;
    @Column(name = "transmission", nullable = false, length = 50)
    private String transmission;
    @Column(name = "price_per_km", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerKm;
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @JsonIgnore
    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();

    public Car() {}
    public Car(Long id, String name, String brand, Integer modelYear, Integer seatingCapacity, String fuelType, String transmission, BigDecimal pricePerKm, String imageUrl, Boolean isAvailable, LocalDateTime createdAt) {
        this.id = id; this.name = name; this.brand = brand; this.modelYear = modelYear; this.seatingCapacity = seatingCapacity; this.fuelType = fuelType; this.transmission = transmission; this.pricePerKm = pricePerKm; this.imageUrl = imageUrl; this.isAvailable = isAvailable; this.createdAt = createdAt;
    }
    @PrePersist protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.isAvailable == null) this.isAvailable = true;
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
    public List<Booking> getBookings() { return bookings; } public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public static CarBuilder builder() { return new CarBuilder(); }
    public static class CarBuilder {
        private Long id; private String name; private String brand; private Integer modelYear; private Integer seatingCapacity; private String fuelType; private String transmission; private BigDecimal pricePerKm; private String imageUrl; private Boolean isAvailable = true; private LocalDateTime createdAt;
        public CarBuilder id(Long id) { this.id = id; return this; }
        public CarBuilder name(String name) { this.name = name; return this; }
        public CarBuilder brand(String brand) { this.brand = brand; return this; }
        public CarBuilder modelYear(Integer modelYear) { this.modelYear = modelYear; return this; }
        public CarBuilder seatingCapacity(Integer seatingCapacity) { this.seatingCapacity = seatingCapacity; return this; }
        public CarBuilder fuelType(String fuelType) { this.fuelType = fuelType; return this; }
        public CarBuilder transmission(String transmission) { this.transmission = transmission; return this; }
        public CarBuilder pricePerKm(BigDecimal pricePerKm) { this.pricePerKm = pricePerKm; return this; }
        public CarBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public CarBuilder isAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; return this; }
        public CarBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Car build() { return new Car(id, name, brand, modelYear, seatingCapacity, fuelType, transmission, pricePerKm, imageUrl, isAvailable, createdAt); }
    }
}