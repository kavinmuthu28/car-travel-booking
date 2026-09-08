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
    public UserService(UserRepository userRepository) { this.userRepository = userRepository; }

    public UserResponseDTO getUserById(Long id) {
        return mapToDTO(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found: " + id)));
    }

    public UserResponseDTO getUserByEmail(String email) {
        return mapToDTO(userRepository.findByEmail(email.trim().toLowerCase()).orElseThrow(() -> new ResourceNotFoundException("User not found: " + email)));
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO req, String email) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        User caller = userRepository.findByEmail(email).orElseThrow(() -> new UnauthorizedException("Session invalid"));
        if (!caller.getId().equals(user.getId()) && caller.getRole() != UserRole.ROLE_ADMIN) throw new UnauthorizedException("Unauthorized");
        if (!user.getPhone().equals(req.getPhone().trim()) && userRepository.existsByPhone(req.getPhone().trim())) throw new DuplicateResourceException("Phone in use");
        user.setName(req.getName().trim());
        user.setPhone(req.getPhone().trim());
        return mapToDTO(userRepository.save(user));
    }

    public UserResponseDTO mapToDTO(User u) {
        return new UserResponseDTO(u.getId(), u.getName(), u.getEmail(), u.getPhone(), u.getRole().name(), u.getCreatedAt());
    }
}