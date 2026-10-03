package com.cartravel.booking.service;

import com.cartravel.booking.dto.user.UserRequestDTO;
import com.cartravel.booking.dto.user.UserResponseDTO;
import com.cartravel.booking.entity.User;
import com.cartravel.booking.enums.UserRole;
import com.cartravel.booking.exception.UnauthorizedException;
import com.cartravel.booking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit & Ownership Tests")
class UserServiceTest {

    @Mock private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User customerA;
    private User customerB;
    private User adminUser;

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

        adminUser = User.builder()
                .id(99L)
                .name("Admin")
                .email("admin@example.com")
                .phone("9876543299")
                .role(UserRole.ROLE_ADMIN)
                .build();
    }

    @Test
    @DisplayName("BUG-006: Customer cannot view another customer's profile")
    void testGetUserById_UnauthorizedCustomer_ThrowsUnauthorized() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(customerA));
        when(userRepository.findByEmail("custB@example.com")).thenReturn(Optional.of(customerB));

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () ->
                userService.getUserById(1L, "custB@example.com"));

        assertTrue(ex.getMessage().contains("Access denied"));
    }

    @Test
    @DisplayName("BUG-006: Customer can view their own profile")
    void testGetUserById_SelfAccess_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(customerA));
        when(userRepository.findByEmail("custA@example.com")).thenReturn(Optional.of(customerA));

        UserResponseDTO res = userService.getUserById(1L, "custA@example.com");

        assertNotNull(res);
        assertEquals(1L, res.getId());
        assertEquals("custA@example.com", res.getEmail());
    }

    @Test
    @DisplayName("BUG-006: Admin can view any customer profile")
    void testGetUserById_AdminAccess_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(customerA));
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(adminUser));

        UserResponseDTO res = userService.getUserById(1L, "admin@example.com");

        assertNotNull(res);
        assertEquals(1L, res.getId());
        assertEquals("custA@example.com", res.getEmail());
    }
}
