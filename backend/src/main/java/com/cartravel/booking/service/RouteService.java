package com.cartravel.booking.service;

import com.cartravel.booking.entity.Destination;
import com.cartravel.booking.repository.DestinationRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class RouteService {
    private final DestinationRepository destinationRepository;
    public RouteService(DestinationRepository destinationRepository) { this.destinationRepository = destinationRepository; }

    public BigDecimal resolveDistance(String pickup, String dropoff, BigDecimal clientDist) {
        if (clientDist != null && clientDist.compareTo(BigDecimal.ZERO) > 0) return clientDist;
        Optional<Destination> dest = destinationRepository.findByFromCityIgnoreCaseAndToCityIgnoreCase(pickup.trim(), dropoff.trim());
        return dest.map(Destination::getDistanceKm).orElse(new BigDecimal("86.00"));
    }
}