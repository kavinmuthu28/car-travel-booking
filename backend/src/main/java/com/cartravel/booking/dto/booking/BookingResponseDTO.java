package com.cartravel.booking.dto.booking;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
public class BookingResponseDTO {
    private Long id; private String bookingNumber; private Long userId; private String customerName; private String customerEmail; private String customerPhone; private Long carId; private String carName; private String carBrand; private BigDecimal carPricePerKm; private String carImageUrl; private Long destinationId; private String pickupAddress; private String dropoffAddress; private LocalDate pickupDate; private LocalTime pickupTime; private String tripType; private BigDecimal totalDistanceKm; private BigDecimal totalAmount; private String bookingStatus; private String specialRequests; private Integer passengerCount; private LocalDateTime createdAt;
    public BookingResponseDTO() {}
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getBookingNumber() { return bookingNumber; } public void setBookingNumber(String bookingNumber) { this.bookingNumber = bookingNumber; }
    public Long getUserId() { return userId; } public void setUserId(Long userId) { this.userId = userId; }
    public String getCustomerName() { return customerName; } public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerEmail() { return customerEmail; } public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public String getCustomerPhone() { return customerPhone; } public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public Long getCarId() { return carId; } public void setCarId(Long carId) { this.carId = carId; }
    public String getCarName() { return carName; } public void setCarName(String carName) { this.carName = carName; }
    public String getCarBrand() { return carBrand; } public void setCarBrand(String carBrand) { this.carBrand = carBrand; }
    public BigDecimal getCarPricePerKm() { return carPricePerKm; } public void setCarPricePerKm(BigDecimal carPricePerKm) { this.carPricePerKm = carPricePerKm; }
    public String getCarImageUrl() { return carImageUrl; } public void setCarImageUrl(String carImageUrl) { this.carImageUrl = carImageUrl; }
    public Long getDestinationId() { return destinationId; } public void setDestinationId(Long destinationId) { this.destinationId = destinationId; }
    public String getPickupAddress() { return pickupAddress; } public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
    public String getDropoffAddress() { return dropoffAddress; } public void setDropoffAddress(String dropoffAddress) { this.dropoffAddress = dropoffAddress; }
    public LocalDate getPickupDate() { return pickupDate; } public void setPickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; }
    public LocalTime getPickupTime() { return pickupTime; } public void setPickupTime(LocalTime pickupTime) { this.pickupTime = pickupTime; }
    public String getTripType() { return tripType; } public void setTripType(String tripType) { this.tripType = tripType; }
    public BigDecimal getTotalDistanceKm() { return totalDistanceKm; } public void setTotalDistanceKm(BigDecimal totalDistanceKm) { this.totalDistanceKm = totalDistanceKm; }
    public BigDecimal getTotalAmount() { return totalAmount; } public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getBookingStatus() { return bookingStatus; } public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public String getSpecialRequests() { return specialRequests; } public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    public Integer getPassengerCount() { return passengerCount; } public void setPassengerCount(Integer passengerCount) { this.passengerCount = passengerCount; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}