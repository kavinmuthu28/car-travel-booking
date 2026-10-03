package com.cartravel.booking.controller;

import com.cartravel.booking.dto.user.UserRequestDTO;
import com.cartravel.booking.dto.user.UserResponseDTO;
import com.cartravel.booking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController — REST endpoints for user management.
 *
 * Security model:
 *   GET  /api/users        → ADMIN ONLY (list all users)
 *   GET  /api/users/{id}   → Own profile (or ADMIN for any)
 *   PUT  /api/users/{id}   → Own profile update (or ADMIN for any)
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * FIX BUG-005: Was accessible to any authenticated user.
     * Now restricted to ADMIN only.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * FIX BUG-006: Any authenticated user could view any other user's profile.
     * Now enforces ownership: only self or ADMIN.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id, Authentication a) {
        return ResponseEntity.ok(userService.getUserById(id, a.getName()));
    }

    /** Update user profile — ownership enforced in service layer */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id, @Valid @RequestBody UserRequestDTO req, Authentication a) {
        return ResponseEntity.ok(userService.updateUser(id, req, a.getName()));
    }
}