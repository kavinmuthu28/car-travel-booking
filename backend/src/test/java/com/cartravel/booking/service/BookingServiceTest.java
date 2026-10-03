package com.cartravel.booking.service;

import com.cartravel.booking.dto.booking.BookingRequestDTO;
import com.cartravel.booking.dto.booking.BookingResponseDTO;
import com.cartravel.booking.entity.Booking;
import com.cartravel.booking.entity.Car;
import com.cartravel.booking.entity.User;
import com.cartravel.booking.enums.BookingStatus;
import com.cartravel.booking.enums.TripType;
import com.cartravel.booking.enums.UserRole;
import com.cartravel.booking.exception.CarUnavailableException;
import com.cartravel.booking.exception.InvalidBookingException;
import com.cartravel.booking.exception.UnauthorizedException;
import com.cartravel.booking.repository.BookingRepository;
import com.cartravel.booking.repository.CarRepository;
import com.cartravel.booking.repository.DestinationRepository;
import com.cartravel.booking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookingService Unit & Security Tests")
class BookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private CarRepository carRepository;
    @Mock private UserRepository userRepository;
    @Mock private DestinationRepository destinationRepository;
    @Mock private FareCalculationService fareCalculationService;
    @Mock private RouteService routeService;

    @InjectMocks
    private BookingService bookingService;

    private User customerA;
    private User customerB;
    private Car testCar;

    @BeforeEach
    void setUp() {
        customerA = User.builder()
                .id(1L)
                .name("Customer A")
                .email("custA@example.com")
                .phone("9876543210")
                .role(UserRole.ROLE_CUSTOMER)
                .build();

        customerB = User.builder()
                .id(2L)
                .name("Customer B")
                .email("custB@example.com")
                .phone("9876543211")
                .role(UserRole.ROLE_CUSTOMER)
                .build();

        testCar = new Car();
        testCar.setId(10L);
        testCar.setName("Swift Dzire");
        testCar.setBrand("Maruti");
        testCar.setPricePerKm(new BigDecimal("14.00"));
        testCar.setIsAvailable(true);
    }

    @Test
    @DisplayName("BUG-012: Reject booking with past pickup date")
    void testCreateBooking_PastDate_ThrowsInvalidBookingException() {
        BookingRequestDTO req = new BookingRequestDTO();
        req.setCarId(10L);
        req.setPickupDate(LocalDate.now().minusDays(1)); // yesterday
        req.setPickupTime(LocalTime.of(10, 0));
        req.setPickupAddress("Coimbatore");
        req.setDropoffAddress("Ooty");

        InvalidBookingException ex = assertThrows(InvalidBookingException.class, () ->
                bookingService.createBooking(req, "custA@example.com"));

        assertTrue(ex.getMessage().contains("past"));
        verifyNoInteractions(bookingRepository);
    }

    @Test
    @DisplayName("BUG-010: Reject booking if car already booked for selected date")
    void testCreateBooking_CarAlreadyBooked_ThrowsCarUnavailable() {
        LocalDate travelDate = LocalDate.now().plusDays(2);
        BookingRequestDTO req = new BookingRequestDTO();
        req.setCarId(10L);
        req.setPickupDate(travelDate);
        req.setPickupTime(LocalTime.of(10, 0));
        req.setPickupAddress("Coimbatore");
        req.setDropoffAddress("Ooty");
        req.setTripType(TripType.ONE_WAY);

        when(userRepository.findByEmail("custA@example.com")).thenReturn(Optional.of(customerA));
        when(carRepository.findById(10L)).thenReturn(Optional.of(testCar));
        when(bookingRepository.existsByCarIdAndPickupDateAndNotCancelled(eq(10L), eq(travelDate), eq(BookingStatus.CANCELLED)))
                .thenReturn(true);

        assertThrows(CarUnavailableException.class, () ->
                bookingService.createBooking(req, "custA@example.com"));
    }

    @Test
    @DisplayName("BUG-003: Enforce ownership on getBookingById - Unauthorized customer rejected")
    void testGetBookingById_WrongUser_ThrowsUnauthorized() {
        Booking booking = Booking.builder()
                .id(100L)
                .bookingNumber("KM-261003-ABCD1234")
                .user(customerA)
                .car(testCar)
                .bookingStatus(BookingStatus.CONFIRMED)
                .build();

        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));
        when(userRepository.findByEmail("custB@example.com")).thenReturn(Optional.of(customerB));

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () ->
                bookingService.getBookingById(100L, "custB@example.com"));

        assertTrue(ex.getMessage().contains("Access denied"));
    }

    @Test
    @DisplayName("BUG-019: Prevent cancelling already completed booking")
    void testCancelBooking_AlreadyCompleted_ThrowsInvalidBookingException() {
        Booking completedBooking = Booking.builder()
                .id(101L)
                .bookingNumber("KM-261003-DONE")
                .user(customerA)
                .car(testCar)
                .bookingStatus(BookingStatus.COMPLETED)
                .build();

        when(bookingRepository.findById(101L)).thenReturn(Optional.of(completedBooking));
        when(userRepository.findByEmail("custA@example.com")).thenReturn(Optional.of(customerA));

        InvalidBookingException ex = assertThrows(InvalidBookingException.class, () ->
                bookingService.cancelBooking(101L, "custA@example.com"));

        assertTrue(ex.getMessage().contains("completed"));
    }

    @Test
    @DisplayName("BUG-019: Prevent cancelling already cancelled booking")
    void testCancelBooking_AlreadyCancelled_ThrowsInvalidBookingException() {
        Booking cancelledBooking = Booking.builder()
                .id(102L)
                .bookingNumber("KM-261003-CANC")
                .user(customerA)
                .car(testCar)
                .bookingStatus(BookingStatus.CANCELLED)
                .build();

        when(bookingRepository.findById(102L)).thenReturn(Optional.of(cancelledBooking));
        when(userRepository.findByEmail("custA@example.com")).thenReturn(Optional.of(customerA));

        InvalidBookingException ex = assertThrows(InvalidBookingException.class, () ->
                bookingService.cancelBooking(102L, "custA@example.com"));

        assertTrue(ex.getMessage().contains("already cancelled"));
    }

    @Test
    @DisplayName("Valid booking creation produces booking with unique number and CONFIRMED status")
    void testCreateBooking_Success() {
        LocalDate travelDate = LocalDate.now().plusDays(3);
        BookingRequestDTO req = new BookingRequestDTO();
        req.setCarId(10L);
        req.setPickupDate(travelDate);
        req.setPickupTime(LocalTime.of(9, 30));
        req.setPickupAddress("Coimbatore");
        req.setDropoffAddress("Ooty");
        req.setTripType(TripType.ONE_WAY);
        req.setClientDistanceKm(new BigDecimal("86.00"));

        when(userRepository.findByEmail("custA@example.com")).thenReturn(Optional.of(customerA));
        when(carRepository.findById(10L)).thenReturn(Optional.of(testCar));
        when(bookingRepository.existsByCarIdAndPickupDateAndNotCancelled(anyLong(), any(), any())).thenReturn(false);
        when(routeService.resolveDistance(anyString(), anyString(), any())).thenReturn(new BigDecimal("86.00"));
        when(fareCalculationService.calculateTotalFare(any(), any(), any())).thenReturn(new BigDecimal("1704.00"));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking saved = invocation.getArgument(0);
            saved.setId(500L);
            return saved;
        });

        BookingResponseDTO response = bookingService.createBooking(req, "custA@example.com");

        assertNotNull(response);
        assertEquals(500L, response.getId());
        assertTrue(response.getBookingNumber().startsWith("KM-"));
        assertEquals("CONFIRMED", response.getBookingStatus());
        assertEquals(customerA.getId(), response.getUserId());
        assertEquals("Coimbatore", response.getPickupAddress());
    }
}
