package com.cartravel.booking.repository;
import com.cartravel.booking.entity.Booking;
import com.cartravel.booking.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingNumber(String bookingNumber);
    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Booking> findByBookingStatusOrderByCreatedAtDesc(BookingStatus bookingStatus);
    List<Booking> findAllByOrderByCreatedAtDesc();
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.car.id = :carId AND b.pickupDate = :pickupDate AND b.bookingStatus NOT IN ('CANCELLED')")
    boolean existsByCarIdAndPickupDateAndNotCancelled(@Param("carId") Long carId, @Param("pickupDate") LocalDate pickupDate);
}