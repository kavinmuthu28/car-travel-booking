package com.cartravel.booking.dto.booking;
import com.cartravel.booking.enums.TripType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
public class BookingRequestDTO {
    @NotNull(message = "Car ID is required") private Long carId;
    private Long destinationId;
    @NotNull(message = "Pickup date is required") private LocalDate pickupDate;
    @NotNull(message = "Pickup time is required") private LocalTime pickupTime;
    @NotBlank(message = "Pickup address is required") private String pickupAddress;
    @NotBlank(message = "Dropoff address is required") private String dropoffAddress;
    @NotNull(message = "Trip type is required") private TripType tripType = TripType.ONE_WAY;
    private Integer passengerCount = 1;
    private BigDecimal clientDistanceKm;
    private String specialRequests;
    public BookingRequestDTO() {}
    public Long getCarId() { return carId; } public void setCarId(Long carId) { this.carId = carId; }
    public Long getDestinationId() { return destinationId; } public void setDestinationId(Long destinationId) { this.destinationId = destinationId; }
    public LocalDate getPickupDate() { return pickupDate; } public void setPickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; }
    public LocalTime getPickupTime() { return pickupTime; } public void setPickupTime(LocalTime pickupTime) { this.pickupTime = pickupTime; }
    public String getPickupAddress() { return pickupAddress; } public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
    public String getDropoffAddress() { return dropoffAddress; } public void setDropoffAddress(String dropoffAddress) { this.dropoffAddress = dropoffAddress; }
    public TripType getTripType() { return tripType; } public void setTripType(TripType tripType) { this.tripType = tripType; }
    public Integer getPassengerCount() { return passengerCount; } public void setPassengerCount(Integer passengerCount) { this.passengerCount = passengerCount; }
    public BigDecimal getClientDistanceKm() { return clientDistanceKm; } public void setClientDistanceKm(BigDecimal clientDistanceKm) { this.clientDistanceKm = clientDistanceKm; }
    public String getSpecialRequests() { return specialRequests; } public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
}