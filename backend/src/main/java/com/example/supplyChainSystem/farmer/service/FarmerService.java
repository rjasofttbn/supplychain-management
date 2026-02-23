package com.example.supplyChainSystem.farmer.service;

import com.example.supplyChainSystem.auth.entity.User;
import com.example.supplyChainSystem.auth.repository.UserRepository;
import com.example.supplyChainSystem.farmer.entity.Farmer;
import com.example.supplyChainSystem.farmer.repository.FarmerRepository;
import com.example.supplyChainSystem.item.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FarmerService {
    private final FarmerRepository farmerRepository;
    private final UserRepository userRepository; // Inject this instead

    public List<Farmer> getAllActive() {
        return farmerRepository.findAllByDeletedAtIsNull();
    }

    public Farmer getById(Long id) {
        return farmerRepository.findById(id)
                .filter(f -> f.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));
    }

    public Farmer create(Farmer farmer, String email) {
        // Check if an ACTIVE farmer already has this phone number
        if (farmerRepository.existsByPhoneAndDeletedAtIsNull(farmer.getPhone())) {
            throw new RuntimeException("This phone number is already registered to an active farmer!");
        }
        // 1. Find the user by the email provided by Principal
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Set the ID (as a String) instead of the email
        farmer.setCreatedBy(Long.valueOf(String.valueOf(user.getId())));

        return farmerRepository.save(farmer);
    }

    public Farmer updateFarmer(Long id, Farmer updatedData) {
        // 1. Find the existing farmer
        Farmer existingFarmer = farmerRepository.findById(id)
                .filter(f -> f.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Farmer not found or is deleted"));

        // 2. Check if the NEW phone number is already taken by ANOTHER farmer
        if (!existingFarmer.getPhone().equals(updatedData.getPhone())) {
            if (farmerRepository.existsByPhoneAndDeletedAtIsNull(updatedData.getPhone())) {
                throw new RuntimeException("Cannot update: This phone number is already in use by another farmer.");
            }
        }

        // 3. Update the fields
        existingFarmer.setName(updatedData.getName());
        existingFarmer.setPhone(updatedData.getPhone());
        existingFarmer.setAddress(updatedData.getAddress());
        // Note: updatedAt is handled automatically by @PreUpdate in the Entity

        // 4. Save changes
        return farmerRepository.save(existingFarmer);
    }
    public void delete(Long id) {
        Farmer farmer = getById(id);
        farmer.setDeletedAt(LocalDateTime.now());
        farmerRepository.save(farmer);
    }

    public List<Farmer> getTrash() {
        return farmerRepository.findAllByDeletedAtIsNotNull();
    }
}