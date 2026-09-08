package com.cartravel.booking.dto.booking;
import com.cartravel.booking.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
public class BookingUpdateDTO {
    @NotNull(message = "Booking status is required") private BookingStatus status;
    public BookingUpdateDTO() {}
    public BookingStatus getStatus() { return status; } public void setStatus(BookingStatus status) { this.status = status; }
}