package com.cartravel.booking.repository;
import com.cartravel.booking.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByIsAvailableTrue();
    List<Car> findBySeatingCapacityGreaterThanEqualAndIsAvailableTrue(Integer minSeats);
    @Query("SELECT c FROM Car c WHERE c.isAvailable = true AND c.id NOT IN (SELECT b.car.id FROM Booking b WHERE b.pickupDate = :date AND b.bookingStatus NOT IN ('CANCELLED'))")
    List<Car> findAvailableCarsForDate(@Param("date") LocalDate date);
}