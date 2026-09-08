package com.cartravel.booking.entity;

import com.cartravel.booking.enums.BookingStatus;
import com.cartravel.booking.enums.BookingStatusConverter;
import com.cartravel.booking.enums.TripType;
import com.cartravel.booking.enums.TripTypeConverter;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "booking_number", nullable = false, unique = true, length = 50)
    private String bookingNumber;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "car_id", nullable = false)
    private Car car;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "destination_id")
    private Destination destination;
    @Column(name = "pickup_date", nullable = false)
    private LocalDate pickupDate;
    @Column(name = "pickup_time", nullable = false)
    private LocalTime pickupTime;
    @Column(name = "pickup_address", nullable = false, length = 255)
    private String pickupAddress;
    @Column(name = "dropoff_address", nullable = false, length = 255)
    private String dropoffAddress;
    @Column(name = "trip_type", nullable = false)
    @Convert(converter = TripTypeConverter.class)
    private TripType tripType;
    @Column(name = "total_distance_km", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalDistanceKm;
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    @Column(name = "booking_status", nullable = false)
    @Convert(converter = BookingStatusConverter.class)
    private BookingStatus bookingStatus;
    @Column(name = "special_requests", columnDefinition = "TEXT")
    private String specialRequests;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    public Booking() {}
    public Booking(Long id, String bookingNumber, User user, Car car, Destination destination, LocalDate pickupDate, LocalTime pickupTime, String pickupAddress, String dropoffAddress, TripType tripType, BigDecimal totalDistanceKm, BigDecimal totalAmount, BookingStatus bookingStatus, String specialRequests, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id; this.bookingNumber = bookingNumber; this.user = user; this.car = car; this.destination = destination; this.pickupDate = pickupDate; this.pickupTime = pickupTime; this.pickupAddress = pickupAddress; this.dropoffAddress = dropoffAddress; this.tripType = tripType; this.totalDistanceKm = totalDistanceKm; this.totalAmount = totalAmount; this.bookingStatus = bookingStatus; this.specialRequests = specialRequests; this.createdAt = createdAt; this.updatedAt = updatedAt;
    }
    @PrePersist protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.bookingStatus == null) this.bookingStatus = BookingStatus.PENDING;
    }
    @PreUpdate protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getBookingNumber() { return bookingNumber; } public void setBookingNumber(String bookingNumber) { this.bookingNumber = bookingNumber; }
    public User getUser() { return user; } public void setUser(User user) { this.user = user; }
    public Car getCar() { return car; } public void setCar(Car car) { this.car = car; }
    public Destination getDestination() { return destination; } public void setDestination(Destination destination) { this.destination = destination; }
    public LocalDate getPickupDate() { return pickupDate; } public void setPickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; }
    public LocalTime getPickupTime() { return pickupTime; } public void setPickupTime(LocalTime pickupTime) { this.pickupTime = pickupTime; }
    public String getPickupAddress() { return pickupAddress; } public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
    public String getDropoffAddress() { return dropoffAddress; } public void setDropoffAddress(String dropoffAddress) { this.dropoffAddress = dropoffAddress; }
    public TripType getTripType() { return tripType; } public void setTripType(TripType tripType) { this.tripType = tripType; }
    public BigDecimal getTotalDistanceKm() { return totalDistanceKm; } public void setTotalDistanceKm(BigDecimal totalDistanceKm) { this.totalDistanceKm = totalDistanceKm; }
    public BigDecimal getTotalAmount() { return totalAmount; } public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BookingStatus getBookingStatus() { return bookingStatus; } public void setBookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; }
    public String getSpecialRequests() { return specialRequests; } public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; } public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<Payment> getPayments() { return payments; } public void setPayments(List<Payment> payments) { this.payments = payments; }

    public static BookingBuilder builder() { return new BookingBuilder(); }
    public static class BookingBuilder {
        private Long id; private String bookingNumber; private User user; private Car car; private Destination destination; private LocalDate pickupDate; private LocalTime pickupTime; private String pickupAddress; private String dropoffAddress; private TripType tripType; private BigDecimal totalDistanceKm; private BigDecimal totalAmount; private BookingStatus bookingStatus; private String specialRequests; private LocalDateTime createdAt; private LocalDateTime updatedAt;
        public BookingBuilder id(Long id) { this.id = id; return this; }
        public BookingBuilder bookingNumber(String bookingNumber) { this.bookingNumber = bookingNumber; return this; }
        public BookingBuilder user(User user) { this.user = user; return this; }
        public BookingBuilder car(Car car) { this.car = car; return this; }
        public BookingBuilder destination(Destination destination) { this.destination = destination; return this; }
        public BookingBuilder pickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; return this; }
        public BookingBuilder pickupTime(LocalTime pickupTime) { this.pickupTime = pickupTime; return this; }
        public BookingBuilder pickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; return this; }
        public BookingBuilder dropoffAddress(String dropoffAddress) { this.dropoffAddress = dropoffAddress; return this; }
        public BookingBuilder tripType(TripType tripType) { this.tripType = tripType; return this; }
        public BookingBuilder totalDistanceKm(BigDecimal totalDistanceKm) { this.totalDistanceKm = totalDistanceKm; return this; }
        public BookingBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public BookingBuilder bookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; return this; }
        public BookingBuilder specialRequests(String specialRequests) { this.specialRequests = specialRequests; return this; }
        public BookingBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public BookingBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public Booking build() { return new Booking(id, bookingNumber, user, car, destination, pickupDate, pickupTime, pickupAddress, dropoffAddress, tripType, totalDistanceKm, totalAmount, bookingStatus, specialRequests, createdAt, updatedAt); }
    }
}