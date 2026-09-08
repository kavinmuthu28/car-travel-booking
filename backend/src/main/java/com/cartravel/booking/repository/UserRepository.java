package com.cartravel.booking.repository;
import com.cartravel.booking.entity.User;
import com.cartravel.booking.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    List<User> findByRole(UserRole role);
}