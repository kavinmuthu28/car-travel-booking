package com.cartravel.booking.controller;

import com.cartravel.booking.dto.destination.DestinationRequestDTO;
import com.cartravel.booking.dto.destination.DestinationResponseDTO;
import com.cartravel.booking.service.DestinationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
public class DestinationController {
    private final DestinationService destinationService;
    public DestinationController(DestinationService destinationService) { this.destinationService = destinationService; }
    @GetMapping public ResponseEntity<List<DestinationResponseDTO>> getAllDestinations() { return ResponseEntity.ok(destinationService.getAllDestinations()); }
    @GetMapping("/{id}") public ResponseEntity<DestinationResponseDTO> getDestinationById(@PathVariable Long id) { return ResponseEntity.ok(destinationService.getDestinationById(id)); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<DestinationResponseDTO> createDestination(@Valid @RequestBody DestinationRequestDTO req) { return new ResponseEntity<>(destinationService.createDestination(req), HttpStatus.CREATED); }
}