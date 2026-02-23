package com.example.supplyChainSystem.auth.service;

import com.example.supplyChainSystem.auth.entity.User;
import com.example.supplyChainSystem.auth.repository.UserRepository;
import com.example.supplyChainSystem.common.service.MailService;
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

    // 3. ADD THIS LINE:
    private final MailService mailService;

//    public User register(User user) {
//        String token = UUID.randomUUID().toString();
//        user.setVerificationToken(token);
//
//        String verificationUrl = "http://localhost:9090/api/auth/verify?token=" + token;
//        System.out.println("VERIFICATION LINK: " + verificationUrl);
//
//        userRepository.save(user);
//
//        // Now 'mailService' will be recognized!
//        try {
//            mailService.sendEmail(user.getEmail(), "Verify", verificationUrl);
//
////            mailService.sendEmail(user.getEmail(), "Subject", "Body");
////            mailService.sendEmail(user.getEmail(), "Verify Email", verificationUrl);
//        } catch (Exception e) {
//            System.out.println("Email error: " + e.getMessage());
//        }
//
//        return user;
//    }

    public void register(User user) {
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setTokenExpiry(LocalDateTime.now().plusHours(24));
        user.setEnabled(false); // Disable until verified
        userRepository.save(user);

        sendVerificationEmail(user);
    }

    // <-- Replace the old method with this one
    private void sendVerificationEmail(User user) {
        // Instead of sending email, just print the link
        String verifyUrl = "http://localhost:9090/api/auth/verify?token=" + user.getVerificationToken();
        System.out.println("Verification link: " + verifyUrl);
    }


//    private void sendVerificationEmail(User user) {
//        try {
//            String verifyUrl = "http://localhost:8080/api/auth/verify?token=" + user.getVerificationToken();
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo(user.getEmail());
//            message.setSubject("Verify your email");
//            message.setText("Click here to verify: " + verifyUrl);
//            mailSender.send(message);
//            System.out.println("Email sent successfully to: " + user.getEmail());
//        } catch (Exception e) {
//            System.err.println("ERROR SENDING EMAIL: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

//    public String verifyToken(String token) {
//        User user = userRepository.findByVerificationToken(token)
//                .orElseThrow(() -> new RuntimeException("Invalid Token"));
//
//        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
//            throw new RuntimeException("Token Expired");
//        }
//
//        user.setEnabled(true);
//        user.setVerificationToken(null); // Clear token
//        userRepository.save(user);
//        return "Email Verified Successfully!";
//    }
public String verifyToken(String token) {
    User user = userRepository.findByVerificationToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid Token"));

    if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
        throw new RuntimeException("Token Expired");
    }

    user.setEnabled(true);
    user.setVerificationToken(null); // Clear token
    user.setEmailVerifiedAt(LocalDateTime.now()); // <-- set the verification time
    userRepository.save(user);

    return "Email Verified Successfully!";
}
}
