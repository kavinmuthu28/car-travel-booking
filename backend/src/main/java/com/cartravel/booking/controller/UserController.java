package com.cartravel.booking.controller;

import com.cartravel.booking.dto.user.UserRequestDTO;
import com.cartravel.booking.dto.user.UserResponseDTO;
import com.cartravel.booking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }
    @GetMapping public ResponseEntity<List<UserResponseDTO>> getAllUsers() { return ResponseEntity.ok(userService.getAllUsers()); }
    @GetMapping("/{id}") public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) { return ResponseEntity.ok(userService.getUserById(id)); }
    @PutMapping("/{id}") public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO req, Authentication a) { return ResponseEntity.ok(userService.updateUser(id, req, a.getName())); }
}