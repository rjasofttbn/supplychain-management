package com.example.supplyChainSystem.auth.repository;

import com.example.supplyChainSystem.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    // ADD THIS LINE TO FIX THE ERROR
    Optional<User> findByVerificationToken(String token);
}

