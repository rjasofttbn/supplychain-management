package com.example.supplyChainSystem.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.example.supplyChainSystem.common.audit.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntity {

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password") // The hashed password
    private String password;

    @Column(name = "clear_password")
    private String clearPassword;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled = false; // User cannot login until true
    private String verificationToken;
    private LocalDateTime tokenExpiry;

    // Explicit getter for safety
    public String getPassword() {
        return password;
    }
}
