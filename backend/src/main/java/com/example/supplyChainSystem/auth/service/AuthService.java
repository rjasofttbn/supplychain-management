package com.example.supplyChainSystem.auth.service;

import com.example.supplyChainSystem.auth.entity.User;
import com.example.supplyChainSystem.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage; // You will need this too

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    public void register(User user) {
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setTokenExpiry(LocalDateTime.now().plusHours(24));
        user.setEnabled(false); // Disable until verified
        userRepository.save(user);

        sendVerificationEmail(user);
    }

    private void sendVerificationEmail(User user) {
        try {
            String verifyUrl = "http://localhost:8080/api/auth/verify?token=" + user.getVerificationToken();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Verify your email");
            message.setText("Click here to verify: " + verifyUrl);
            mailSender.send(message);
            System.out.println("Email sent successfully to: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("ERROR SENDING EMAIL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String verifyToken(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid Token"));

        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token Expired");
        }

        user.setEnabled(true);
        user.setVerificationToken(null); // Clear token
        userRepository.save(user);
        return "Email Verified Successfully!";
    }
}
