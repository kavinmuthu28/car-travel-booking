package com.cartravel.booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "destinations")
public class Destination {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "from_city", nullable = false, length = 100)
    private String fromCity;
    @Column(name = "to_city", nullable = false, length = 100)
    private String toCity;
    @Column(name = "distance_km", nullable = false, precision = 10, scale = 2)
    private BigDecimal distanceKm;
    @Column(name = "estimated_duration", nullable = false, length = 50)
    private String estimatedDuration;
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @JsonIgnore
    @OneToMany(mappedBy = "destination", fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();

    public Destination() {}
    public Destination(Long id, String fromCity, String toCity, BigDecimal distanceKm, String estimatedDuration, String imageUrl, String description, LocalDateTime createdAt) {
        this.id = id; this.fromCity = fromCity; this.toCity = toCity; this.distanceKm = distanceKm; this.estimatedDuration = estimatedDuration; this.imageUrl = imageUrl; this.description = description; this.createdAt = createdAt;
    }
    @PrePersist protected void onCreate() { if (this.createdAt == null) this.createdAt = LocalDateTime.now(); }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getFromCity() { return fromCity; } public void setFromCity(String fromCity) { this.fromCity = fromCity; }
    public String getToCity() { return toCity; } public void setToCity(String toCity) { this.toCity = toCity; }
    public BigDecimal getDistanceKm() { return distanceKm; } public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }
    public String getEstimatedDuration() { return estimatedDuration; } public void setEstimatedDuration(String estimatedDuration) { this.estimatedDuration = estimatedDuration; }
    public String getImageUrl() { return imageUrl; } public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<Booking> getBookings() { return bookings; } public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public static DestinationBuilder builder() { return new DestinationBuilder(); }
    public static class DestinationBuilder {
        private Long id; private String fromCity; private String toCity; private BigDecimal distanceKm; private String estimatedDuration; private String imageUrl; private String description; private LocalDateTime createdAt;
        public DestinationBuilder id(Long id) { this.id = id; return this; }
        public DestinationBuilder fromCity(String fromCity) { this.fromCity = fromCity; return this; }
        public DestinationBuilder toCity(String toCity) { this.toCity = toCity; return this; }
        public DestinationBuilder distanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; return this; }
        public DestinationBuilder estimatedDuration(String estimatedDuration) { this.estimatedDuration = estimatedDuration; return this; }
        public DestinationBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public DestinationBuilder description(String description) { this.description = description; return this; }
        public DestinationBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Destination build() { return new Destination(id, fromCity, toCity, distanceKm, estimatedDuration, imageUrl, description, createdAt); }
    }
}