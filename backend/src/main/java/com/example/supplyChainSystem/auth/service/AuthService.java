package com.example.supplyChainSystem.auth.service;

import com.example.supplyChainSystem.auth.entity.User;
import com.example.supplyChainSystem.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage; // You will need this too
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;


    @Transactional
    public void register(User user) {
        // 1. Prepare user data
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setTokenExpiry(LocalDateTime.now().plusHours(24));
        user.setEnabled(false);

        // 2. Save to database (This stays if no exception escapes this method)
        userRepository.save(user);

        // 3. Try to send email, but swallow the error
        try {
            sendVerificationEmail(user);
        } catch (Exception e) {
            // We only print the error so the Transaction can still COMMIT
            System.err.println("CRITICAL: User was saved but email failed. Reason: " + e.getMessage());
        }
    }

//    @Transactional
//    public void register(User user) {
//        user.setVerificationToken(UUID.randomUUID().toString());
//        user.setTokenExpiry(LocalDateTime.now().plusHours(24));
//        user.setEnabled(false); // Disable until verified
//        userRepository.save(user);
//
//        try {
//            sendVerificationEmail(user);
//        } catch (Exception e) {
//            // Log the error but don't re-throw it
//            System.err.println("User saved, but email failed: " + e.getMessage());
//        }
//    }

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
