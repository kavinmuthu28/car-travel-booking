package com.cartravel.booking.controller;

import com.cartravel.booking.dto.car.CarRequestDTO;
import com.cartravel.booking.dto.car.CarResponseDTO;
import com.cartravel.booking.service.CarService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;
    public CarController(CarService carService) { this.carService = carService; }
    @GetMapping public ResponseEntity<List<CarResponseDTO>> getAllCars() { return ResponseEntity.ok(carService.getAllCars()); }
    @GetMapping("/available") public ResponseEntity<List<CarResponseDTO>> getAvailableCars(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam(required = false) Integer passengers) { return ResponseEntity.ok(carService.getAvailableCars(date, passengers)); }
    @GetMapping("/{id}") public ResponseEntity<CarResponseDTO> getCarById(@PathVariable Long id) { return ResponseEntity.ok(carService.getCarById(id)); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<CarResponseDTO> createCar(@Valid @RequestBody CarRequestDTO req) { return new ResponseEntity<>(carService.createCar(req), HttpStatus.CREATED); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<CarResponseDTO> updateCar(@PathVariable Long id, @Valid @RequestBody CarRequestDTO req) { return ResponseEntity.ok(carService.updateCar(id, req)); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<Void> deleteCar(@PathVariable Long id) { carService.deleteCar(id); return ResponseEntity.noContent().build(); }
}