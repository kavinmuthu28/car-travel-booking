package com.cartravel.booking.config;

import com.cartravel.booking.entity.User;
import com.cartravel.booking.enums.UserRole;
import com.cartravel.booking.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * AdminUserInitializer — Seeds the default admin account on startup.
 *
 * SECURITY NOTE (BUG-021/BUG-029):
 * Admin credentials should NEVER be hardcoded in source code.
 * Use environment variables ADMIN_EMAIL and ADMIN_PASSWORD.
 *
 * Set before starting the application:
 *   PowerShell: $env:ADMIN_EMAIL="admin@yourdomain.com"; $env:ADMIN_PASSWORD="YourSecurePassword!"
 *   Linux/Mac:  export ADMIN_EMAIL=admin@yourdomain.com; export ADMIN_PASSWORD=YourSecurePassword!
 *
 * The application will NOT seed an admin account if these env vars are missing,
 * to prevent an insecure default admin from being created.
 */
@Component
public class AdminUserInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(AdminUserInitializer.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_EMAIL:}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Value("${ADMIN_PHONE:+910000000000}")
    private String adminPhone;

    public AdminUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // FIX BUG-021/BUG-029: Do not seed admin if env vars are not set
        if (adminEmail == null || adminEmail.isBlank()) {
            log.warn("ADMIN_EMAIL environment variable not set. Skipping admin account seeding.");
            log.warn("Set ADMIN_EMAIL and ADMIN_PASSWORD environment variables to create/update the admin account.");
            return;
        }
        if (adminPassword == null || adminPassword.isBlank()) {
            log.warn("ADMIN_PASSWORD environment variable not set. Skipping admin account seeding.");
            return;
        }

        Optional<User> existingAdmin = userRepository.findByEmail(adminEmail.trim().toLowerCase());
        if (existingAdmin.isPresent()) {
            User admin = existingAdmin.get();
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(UserRole.ROLE_ADMIN);
            userRepository.save(admin);
            log.info("Admin account updated for: {}", adminEmail);
        } else {
            User admin = User.builder()
                    .name("Admin")
                    .email(adminEmail.trim().toLowerCase())
                    .password(passwordEncoder.encode(adminPassword))
                    .phone(adminPhone.trim())
                    .role(UserRole.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("Admin account seeded for: {}", adminEmail);
        }
    }
}