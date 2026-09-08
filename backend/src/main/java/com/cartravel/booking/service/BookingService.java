package com.cartravel.booking.service;

import com.cartravel.booking.dto.booking.BookingRequestDTO;
import com.cartravel.booking.dto.booking.BookingResponseDTO;
import com.cartravel.booking.dto.booking.BookingUpdateDTO;
import com.cartravel.booking.entity.Booking;
import com.cartravel.booking.entity.Car;
import com.cartravel.booking.entity.Destination;
import com.cartravel.booking.entity.User;
import com.cartravel.booking.enums.BookingStatus;
import com.cartravel.booking.enums.UserRole;
import com.cartravel.booking.exception.BookingNotFoundException;
import com.cartravel.booking.exception.CarNotFoundException;
import com.cartravel.booking.exception.CarUnavailableException;
import com.cartravel.booking.exception.UnauthorizedException;
import com.cartravel.booking.repository.BookingRepository;
import com.cartravel.booking.repository.CarRepository;
import com.cartravel.booking.repository.DestinationRepository;
import com.cartravel.booking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;
    private final FareCalculationService fareCalculationService;
    private final RouteService routeService;

    public BookingService(BookingRepository bookingRepository, CarRepository carRepository, UserRepository userRepository, DestinationRepository destinationRepository, FareCalculationService fareCalculationService, RouteService routeService) {
        this.bookingRepository = bookingRepository; this.carRepository = carRepository; this.userRepository = userRepository; this.destinationRepository = destinationRepository; this.fareCalculationService = fareCalculationService; this.routeService = routeService;
    }

    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO req, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UnauthorizedException("User not found: " + userEmail));
        Car car = carRepository.findById(req.getCarId()).orElseThrow(() -> new CarNotFoundException("Car not found: " + req.getCarId()));
        if (!car.getIsAvailable()) throw new CarUnavailableException("Car unavailable");
        if (bookingRepository.existsByCarIdAndPickupDateAndNotCancelled(car.getId(), req.getPickupDate())) throw new CarUnavailableException("Car already booked on this date");

        BigDecimal dist = routeService.resolveDistance(req.getPickupAddress(), req.getDropoffAddress(), req.getClientDistanceKm());
        BigDecimal total = fareCalculationService.calculateTotalFare(dist, car.getPricePerKm(), req.getTripType());
        Destination dest = req.getDestinationId() != null ? destinationRepository.findById(req.getDestinationId()).orElse(null) : null;

        String bNum = "KM-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + "-" + (1000 + new Random().nextInt(9000));
        Booking b = Booking.builder().bookingNumber(bNum).user(user).car(car).destination(dest).pickupDate(req.getPickupDate()).pickupTime(req.getPickupTime()).pickupAddress(req.getPickupAddress().trim()).dropoffAddress(req.getDropoffAddress().trim()).tripType(req.getTripType()).totalDistanceKm(dist).totalAmount(total).bookingStatus(BookingStatus.CONFIRMED).specialRequests(req.getSpecialRequests()).build();
        return mapToDTO(bookingRepository.save(b));
    }

    public List<BookingResponseDTO> getAllBookings() { return bookingRepository.findAllByOrderByCreatedAtDesc().stream().map(this::mapToDTO).collect(Collectors.toList()); }
    public List<BookingResponseDTO> getBookingsByUserId(Long uid) { return bookingRepository.findByUserIdOrderByCreatedAtDesc(uid).stream().map(this::mapToDTO).collect(Collectors.toList()); }
    public BookingResponseDTO getBookingById(Long id) { return mapToDTO(bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking not found: " + id))); }

    @Transactional
    public BookingResponseDTO updateBookingStatus(Long id, BookingUpdateDTO req) {
        Booking b = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking not found: " + id));
        b.setBookingStatus(req.getStatus());
        return mapToDTO(bookingRepository.save(b));
    }

    @Transactional
    public BookingResponseDTO cancelBooking(Long id, String email) {
        Booking b = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking not found: " + id));
        User u = userRepository.findByEmail(email).orElseThrow(() -> new UnauthorizedException("Session invalid"));
        if (!b.getUser().getId().equals(u.getId()) && u.getRole() != UserRole.ROLE_ADMIN) throw new UnauthorizedException("Not authorized to cancel");
        b.setBookingStatus(BookingStatus.CANCELLED);
        return mapToDTO(bookingRepository.save(b));
    }

    public BookingResponseDTO mapToDTO(Booking b) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(b.getId()); dto.setBookingNumber(b.getBookingNumber());
        if (b.getUser() != null) { dto.setUserId(b.getUser().getId()); dto.setCustomerName(b.getUser().getName()); dto.setCustomerEmail(b.getUser().getEmail()); dto.setCustomerPhone(b.getUser().getPhone()); }
        if (b.getCar() != null) { dto.setCarId(b.getCar().getId()); dto.setCarName(b.getCar().getName()); dto.setCarBrand(b.getCar().getBrand()); dto.setCarPricePerKm(b.getCar().getPricePerKm()); dto.setCarImageUrl(b.getCar().getImageUrl()); }
        if (b.getDestination() != null) dto.setDestinationId(b.getDestination().getId());
        dto.setPickupAddress(b.getPickupAddress()); dto.setDropoffAddress(b.getDropoffAddress()); dto.setPickupDate(b.getPickupDate()); dto.setPickupTime(b.getPickupTime());
        dto.setTripType(b.getTripType() != null ? b.getTripType().name() : "ONE_WAY");
        dto.setTotalDistanceKm(b.getTotalDistanceKm()); dto.setTotalAmount(b.getTotalAmount());
        dto.setBookingStatus(b.getBookingStatus() != null ? b.getBookingStatus().name() : "PENDING");
        dto.setSpecialRequests(b.getSpecialRequests()); dto.setCreatedAt(b.getCreatedAt());
        return dto;
    }
}