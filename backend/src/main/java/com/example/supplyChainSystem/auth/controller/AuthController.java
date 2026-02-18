package com.example.supplyChainSystem.auth.controller;

import com.example.supplyChainSystem.auth.dto.LoginRequestDto;
import com.example.supplyChainSystem.auth.dto.LoginResponseDto;
import com.example.supplyChainSystem.auth.entity.User;
import com.example.supplyChainSystem.auth.repository.UserRepository;
import com.example.supplyChainSystem.auth.service.AuthService;
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
    // 1. You MUST declare the service here
    private final AuthService authService;
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody User user) {
//        // 1. Capture the raw password from the request
//        String rawPassword = user.getPassword();
//
//        // 2. Set the cleartext field (ensure this field exists in your User entity)
//        user.setClearPassword(rawPassword);
//
//        // 3. Encode the password for the security field
//        user.setPassword(passwordEncoder.encode(rawPassword));
//
//        // 4. Save the entity with both fields populated
//        userRepository.save(user);
//
//        return ResponseEntity.ok("User Registered Successfully with clear password saved.");
//    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        // 1. Prepare passwords
        String rawPassword = user.getPassword();
        user.setClearPassword(rawPassword);
        user.setPassword(passwordEncoder.encode(rawPassword));

        // 2. CALL THE SERVICE (This handles token generation & email sending)
        authService.register(user);

        // 3. Return a response that includes the token for testing purposes
        String verificationLink = "http://localhost:8080/api/auth/verify?token=" + user.getVerificationToken();

        return ResponseEntity.ok("User Registered Successfully. Verification Link: " + verificationLink);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        // 1. Find the user first
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. NOW you can check if they are enabled
        if (!user.isEnabled()) {
            throw new RuntimeException("Please verify your email before logging in.");
        }

        // 3. Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 4. Generate token (Pass the whole user object as we configured before)
//        String token = jwtService.generateToken(String.valueOf(user));
        // AuthController.java -> Inside login method
        String token = jwtService.generateToken(user.getEmail()); // Use .getEmail()

        return ResponseEntity.ok(new LoginResponseDto(token, user.getEmail(), user.getRole().name()));
    }


    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        // Now 'authService' will be resolved correctly
        return ResponseEntity.ok(authService.verifyToken(token));
    }
}
