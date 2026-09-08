package com.cartravel.booking.controller;

import com.cartravel.booking.dto.auth.LoginRequestDTO;
import com.cartravel.booking.dto.auth.LoginResponseDTO;
import com.cartravel.booking.dto.auth.RegisterRequestDTO;
import com.cartravel.booking.dto.user.UserResponseDTO;
import com.cartravel.booking.service.AuthService;
import com.cartravel.booking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    public AuthController(AuthService authService, UserService userService) { this.authService = authService; this.userService = userService; }

    @PostMapping("/register") public ResponseEntity<LoginResponseDTO> register(@Valid @RequestBody RegisterRequestDTO req) { return new ResponseEntity<>(authService.register(req), HttpStatus.CREATED); }
    @PostMapping("/login") public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO req) { return ResponseEntity.ok(authService.login(req)); }
    @GetMapping("/me") public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication a) { return ResponseEntity.ok(userService.getUserByEmail(a.getName())); }
}