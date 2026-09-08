package com.cartravel.booking.config;

import com.cartravel.booking.entity.User;
import com.cartravel.booking.enums.UserRole;
import com.cartravel.booking.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminUserInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(AdminUserInitializer.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Optional<User> existingAdmin = userRepository.findByEmail("kavinmuthu84@gmail.com");
        if (existingAdmin.isPresent()) {
            User admin = existingAdmin.get();
            admin.setPassword(passwordEncoder.encode("kavinhari@03"));
            admin.setRole(UserRole.ROLE_ADMIN);
            userRepository.save(admin);
            log.info("Admin password explicitly synchronized to 'kavinhari@03'.");
        } else {
            User admin = User.builder()
                    .name("kavin")
                    .email("kavinmuthu84@gmail.com")
                    .password(passwordEncoder.encode("kavinhari@03"))
                    .phone("+918072007218")
                    .role(UserRole.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("Default Admin account seeded: kavinmuthu84@gmail.com");
        }
    }
}