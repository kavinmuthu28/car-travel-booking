package com.cartravel.booking.service;

import com.cartravel.booking.dto.user.UserRequestDTO;
import com.cartravel.booking.dto.user.UserResponseDTO;
import com.cartravel.booking.entity.User;
import com.cartravel.booking.enums.UserRole;
import com.cartravel.booking.exception.DuplicateResourceException;
import com.cartravel.booking.exception.ResourceNotFoundException;
import com.cartravel.booking.exception.UnauthorizedException;
import com.cartravel.booking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * FIX BUG-006: Enforce ownership — user can only view their own profile; admin can view any.
     */
    public UserResponseDTO getUserById(Long id, String callerEmail) {
        User target = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        User caller = userRepository.findByEmail(callerEmail)
                .orElseThrow(() -> new UnauthorizedException("Session invalid"));
        if (!caller.getId().equals(target.getId()) && caller.getRole() != UserRole.ROLE_ADMIN) {
            throw new UnauthorizedException("Access denied: you can only view your own profile.");
        }
        return mapToDTO(target);
    }

    public UserResponseDTO getUserByEmail(String email) {
        return mapToDTO(userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email)));
    }

    /** ADMIN ONLY — caller enforcement is done at controller level via @PreAuthorize */
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO req, String email) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        User caller = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Session invalid"));
        if (!caller.getId().equals(user.getId()) && caller.getRole() != UserRole.ROLE_ADMIN) {
            throw new UnauthorizedException("Unauthorized: you can only edit your own profile.");
        }
        if (!user.getPhone().equals(req.getPhone().trim())
                && userRepository.existsByPhone(req.getPhone().trim())) {
            throw new DuplicateResourceException("Phone number is already in use.");
        }
        user.setName(req.getName().trim());
        user.setPhone(req.getPhone().trim());
        return mapToDTO(userRepository.save(user));
    }

    public UserResponseDTO mapToDTO(User u) {
        return new UserResponseDTO(u.getId(), u.getName(), u.getEmail(),
                u.getPhone(), u.getRole().name(), u.getCreatedAt());
    }
}