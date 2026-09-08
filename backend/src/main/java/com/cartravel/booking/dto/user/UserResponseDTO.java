package com.cartravel.booking.dto.user;
import java.time.LocalDateTime;
public class UserResponseDTO {
    private Long id; private String name; private String email; private String phone; private String role; private LocalDateTime createdAt;
    public UserResponseDTO() {}
    public UserResponseDTO(Long id, String name, String email, String phone, String role, LocalDateTime createdAt) {
        this.id = id; this.name = name; this.email = email; this.phone = phone; this.role = role; this.createdAt = createdAt;
    }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; } public void setPhone(String phone) { this.phone = phone; }
    public String getRole() { return role; } public void setRole(String role) { this.role = role; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}