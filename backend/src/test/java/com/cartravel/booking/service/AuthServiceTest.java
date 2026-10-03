package com.cartravel.booking.service;

import com.cartravel.booking.dto.auth.LoginRequestDTO;
import com.cartravel.booking.dto.auth.LoginResponseDTO;
import com.cartravel.booking.dto.auth.RegisterRequestDTO;
import com.cartravel.booking.entity.User;
import com.cartravel.booking.enums.UserRole;
import com.cartravel.booking.exception.DuplicateResourceException;
import com.cartravel.booking.repository.UserRepository;
import com.cartravel.booking.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequestDTO registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequestDTO();
        registerRequest.setName("Test User");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("SecretPass123!");
        registerRequest.setPhone("9876543210");
    }

    @Test
    @DisplayName("Registration succeeds with unique email and phone")
    void testRegister_Success() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByPhone("9876543210")).thenReturn(false);
        when(passwordEncoder.encode("SecretPass123!")).thenReturn("hashedPassword123");

        User savedUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .phone("9876543210")
                .role(UserRole.ROLE_CUSTOMER)
                .build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateTokenFromEmail("test@example.com")).thenReturn("mock.jwt.token");

        LoginResponseDTO response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("mock.jwt.token", response.getToken());
        assertEquals("ROLE_CUSTOMER", response.getRole());
        assertEquals("test@example.com", response.getEmail());
    }

    @Test
    @DisplayName("Registration rejected when email already exists")
    void testRegister_DuplicateEmail_ThrowsDuplicateResource() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        DuplicateResourceException ex = assertThrows(DuplicateResourceException.class, () ->
                authService.register(registerRequest));

        assertTrue(ex.getMessage().contains("Email already in use"));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Registration rejected when phone already exists")
    void testRegister_DuplicatePhone_ThrowsDuplicateResource() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByPhone("9876543210")).thenReturn(true);

        DuplicateResourceException ex = assertThrows(DuplicateResourceException.class, () ->
                authService.register(registerRequest));

        assertTrue(ex.getMessage().contains("Phone number already in use"));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Login succeeds with valid credentials and issues JWT token")
    void testLogin_Success() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setEmail("test@example.com");
        req.setPassword("SecretPass123!");

        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);

        User existingUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .role(UserRole.ROLE_CUSTOMER)
                .build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingUser));
        when(jwtService.generateToken(mockAuth)).thenReturn("valid.jwt.token");

        LoginResponseDTO res = authService.login(req);

        assertNotNull(res);
        assertEquals("valid.jwt.token", res.getToken());
        assertEquals("Bearer", res.getType());
        assertEquals("test@example.com", res.getEmail());
    }
}
