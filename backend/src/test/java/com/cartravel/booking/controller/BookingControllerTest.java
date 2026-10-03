package com.cartravel.booking.controller;

import com.cartravel.booking.dto.booking.BookingResponseDTO;
import com.cartravel.booking.dto.booking.FareResponseDTO;
import com.cartravel.booking.enums.TripType;
import com.cartravel.booking.security.CustomUserDetailsService;
import com.cartravel.booking.security.JwtAuthenticationFilter;
import com.cartravel.booking.security.JwtService;
import com.cartravel.booking.service.BookingService;
import com.cartravel.booking.service.FareCalculationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc(addFilters = false) // Unit test for controller endpoints mapping
@DisplayName("BookingController Endpoint Mapping Tests")
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private BookingService bookingService;
    @MockBean private FareCalculationService fareCalculationService;
    @MockBean private JwtService jwtService;
    @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("Public estimate-fare endpoint returns 200 OK")
    void testEstimateFareEndpoint() throws Exception {
        FareResponseDTO fareDto = new FareResponseDTO(
                new BigDecimal("100"), new BigDecimal("15"),
                new BigDecimal("1.0"), new BigDecimal("500"),
                new BigDecimal("1500"), new BigDecimal("2000"));

        when(fareCalculationService.estimateFare(any(), any(), any())).thenReturn(fareDto);

        mockMvc.perform(post("/api/bookings/estimate-fare")
                        .param("distanceKm", "100")
                        .param("pricePerKm", "15")
                        .param("tripType", "ONE_WAY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseFare").value(1500))
                .andExpect(jsonPath("$.totalEstimatedFare").value(2000));
    }

    @Test
    @DisplayName("Get all bookings returns 200 with list")
    void testGetAllBookingsEndpoint() throws Exception {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(1L);
        dto.setBookingNumber("KM-261003-TEST");
        when(bookingService.getAllBookings()).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingNumber").value("KM-261003-TEST"));
    }
}
