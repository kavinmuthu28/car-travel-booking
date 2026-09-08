package com.cartravel.booking.service;

import com.cartravel.booking.dto.destination.DestinationRequestDTO;
import com.cartravel.booking.dto.destination.DestinationResponseDTO;
import com.cartravel.booking.entity.Destination;
import com.cartravel.booking.exception.ResourceNotFoundException;
import com.cartravel.booking.repository.DestinationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DestinationService {
    private final DestinationRepository destinationRepository;
    public DestinationService(DestinationRepository destinationRepository) { this.destinationRepository = destinationRepository; }

    public List<DestinationResponseDTO> getAllDestinations() { return destinationRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList()); }
    public DestinationResponseDTO getDestinationById(Long id) { return mapToDTO(destinationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Destination not found: " + id))); }

    @Transactional
    public DestinationResponseDTO createDestination(DestinationRequestDTO req) {
        Destination d = Destination.builder().fromCity(req.getFromCity().trim()).toCity(req.getToCity().trim()).distanceKm(req.getDistanceKm()).estimatedDuration(req.getEstimatedDuration().trim()).imageUrl(req.getImageUrl()).description(req.getDescription()).build();
        return mapToDTO(destinationRepository.save(d));
    }

    public DestinationResponseDTO mapToDTO(Destination d) {
        return new DestinationResponseDTO(d.getId(), d.getFromCity(), d.getToCity(), d.getDistanceKm(), d.getEstimatedDuration(), d.getImageUrl(), d.getDescription(), d.getCreatedAt());
    }
}