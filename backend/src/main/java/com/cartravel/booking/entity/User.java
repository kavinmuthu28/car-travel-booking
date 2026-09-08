package com.cartravel.booking.entity;

import com.cartravel.booking.enums.RoleConverter;
import com.cartravel.booking.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;
    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;
    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private String phone;
    @Column(name = "role", nullable = false)
    @Convert(converter = RoleConverter.class)
    private UserRole role = UserRole.ROLE_CUSTOMER;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();

    public User() {}
    public User(Long id, String name, String email, String password, String phone, UserRole role, LocalDateTime createdAt) {
        this.id = id; this.name = name; this.email = email; this.password = password; this.phone = phone; this.role = role != null ? role : UserRole.ROLE_CUSTOMER; this.createdAt = createdAt;
    }
    @PrePersist protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.role == null) this.role = UserRole.ROLE_CUSTOMER;
    }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; } public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; } public void setPhone(String phone) { this.phone = phone; }
    public UserRole getRole() { return role; } public void setRole(UserRole role) { this.role = role; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<Booking> getBookings() { return bookings; } public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public static UserBuilder builder() { return new UserBuilder(); }
    public static class UserBuilder {
        private Long id; private String name; private String email; private String password; private String phone; private UserRole role = UserRole.ROLE_CUSTOMER; private LocalDateTime createdAt;
        public UserBuilder id(Long id) { this.id = id; return this; }
        public UserBuilder name(String name) { this.name = name; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder phone(String phone) { this.phone = phone; return this; }
        public UserBuilder role(UserRole role) { this.role = role; return this; }
        public UserBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public User build() { return new User(id, name, email, password, phone, role, createdAt); }
    }
}