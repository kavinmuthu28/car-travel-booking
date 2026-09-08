package com.cartravel.booking.service;

import com.cartravel.booking.dto.auth.LoginRequestDTO;
import com.cartravel.booking.dto.auth.LoginResponseDTO;
import com.cartravel.booking.dto.auth.RegisterRequestDTO;
import com.cartravel.booking.entity.User;
import com.cartravel.booking.enums.UserRole;
import com.cartravel.booking.exception.DuplicateResourceException;
import com.cartravel.booking.exception.ResourceNotFoundException;
import com.cartravel.booking.repository.UserRepository;
import com.cartravel.booking.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository; this.passwordEncoder = passwordEncoder; this.authenticationManager = authenticationManager; this.jwtService = jwtService;
    }

    @Transactional
    public LoginResponseDTO register(RegisterRequestDTO request) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) throw new DuplicateResourceException("Email already in use.");
        if (userRepository.existsByPhone(request.getPhone().trim())) throw new DuplicateResourceException("Phone number already in use.");

        User user = User.builder().name(request.getName().trim()).email(email).password(passwordEncoder.encode(request.getPassword())).phone(request.getPhone().trim()).role(UserRole.ROLE_CUSTOMER).build();
        User saved = userRepository.save(user);
        String token = jwtService.generateTokenFromEmail(saved.getEmail());
        return new LoginResponseDTO(token, "Bearer", saved.getId(), saved.getName(), saved.getEmail(), saved.getRole().name());
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        String email = request.getEmail().trim().toLowerCase();
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
        String token = jwtService.generateToken(auth);
        return new LoginResponseDTO(token, "Bearer", user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}