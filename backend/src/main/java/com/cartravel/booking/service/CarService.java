package com.cartravel.booking.service;

import com.cartravel.booking.dto.car.CarRequestDTO;
import com.cartravel.booking.dto.car.CarResponseDTO;
import com.cartravel.booking.entity.Car;
import com.cartravel.booking.exception.CarNotFoundException;
import com.cartravel.booking.repository.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {
    private final CarRepository carRepository;
    public CarService(CarRepository carRepository) { this.carRepository = carRepository; }

    public List<CarResponseDTO> getAllCars() { return carRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList()); }

    public List<CarResponseDTO> getAvailableCars(LocalDate date, Integer passengers) {
        List<Car> cars = date != null ? carRepository.findAvailableCarsForDate(date) : carRepository.findByIsAvailableTrue();
        if (passengers != null && passengers > 0) cars = cars.stream().filter(c -> c.getSeatingCapacity() >= passengers).collect(Collectors.toList());
        return cars.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public CarResponseDTO getCarById(Long id) {
        return mapToDTO(carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found: " + id)));
    }

    @Transactional
    public CarResponseDTO createCar(CarRequestDTO req) {
        Car car = Car.builder().name(req.getName().trim()).brand(req.getBrand().trim()).modelYear(req.getModelYear()).seatingCapacity(req.getSeatingCapacity()).fuelType(req.getFuelType().toLowerCase().trim()).transmission(req.getTransmission().toLowerCase().trim()).pricePerKm(req.getPricePerKm()).imageUrl(req.getImageUrl()).isAvailable(req.getIsAvailable() != null ? req.getIsAvailable() : true).build();
        return mapToDTO(carRepository.save(car));
    }

    @Transactional
    public CarResponseDTO updateCar(Long id, CarRequestDTO req) {
        Car car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found: " + id));
        car.setName(req.getName().trim()); car.setBrand(req.getBrand().trim()); car.setModelYear(req.getModelYear()); car.setSeatingCapacity(req.getSeatingCapacity()); car.setFuelType(req.getFuelType().toLowerCase().trim()); car.setTransmission(req.getTransmission().toLowerCase().trim()); car.setPricePerKm(req.getPricePerKm()); car.setImageUrl(req.getImageUrl());
        if (req.getIsAvailable() != null) car.setIsAvailable(req.getIsAvailable());
        return mapToDTO(carRepository.save(car));
    }

    @Transactional
    public void deleteCar(Long id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found: " + id));
        carRepository.delete(car);
    }

    public CarResponseDTO mapToDTO(Car c) {
        return new CarResponseDTO(c.getId(), c.getName(), c.getBrand(), c.getModelYear(), c.getSeatingCapacity(), c.getFuelType(), c.getTransmission(), c.getPricePerKm(), c.getImageUrl(), c.getIsAvailable(), c.getCreatedAt());
    }
}