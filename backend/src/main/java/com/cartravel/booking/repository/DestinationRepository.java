package com.cartravel.booking.repository;
import com.cartravel.booking.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    Optional<Destination> findByFromCityIgnoreCaseAndToCityIgnoreCase(String fromCity, String toCity);
    List<Destination> findByFromCityIgnoreCase(String fromCity);
}