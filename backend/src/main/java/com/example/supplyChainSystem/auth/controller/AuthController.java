package com.example.supplyChainSystem.auth.controller;

import com.example.supplyChainSystem.auth.dto.LoginRequestDto;
import com.example.supplyChainSystem.auth.dto.LoginResponseDto;
import com.example.supplyChainSystem.auth.entity.User;
import com.example.supplyChainSystem.auth.repository.UserRepository;
import com.example.supplyChainSystem.common.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        // 1. Capture the raw password from the request
        String rawPassword = user.getPassword();

        // 2. Set the cleartext field (ensure this field exists in your User entity)
        user.setClearPassword(rawPassword);

        // 3. Encode the password for the security field
        user.setPassword(passwordEncoder.encode(rawPassword));

        // 4. Save the entity with both fields populated
        userRepository.save(user);

        return ResponseEntity.ok("User Registered Successfully with clear password saved.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail());

        return ResponseEntity.ok(new LoginResponseDto(token, user.getEmail(), user.getRole().name()));
    }
}
