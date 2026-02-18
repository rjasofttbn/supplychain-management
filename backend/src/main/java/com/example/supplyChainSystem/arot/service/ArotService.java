package com.example.supplyChainSystem.arot.service;

import com.example.supplyChainSystem.arot.entity.Arot;
import com.example.supplyChainSystem.arot.repository.ArotRepository;
//import com.example.supplyChainSystem.user.entity.User;
//import com.example.supplyChainSystem.user.repository.UserRepository;
import com.example.supplyChainSystem.auth.entity.User;
import com.example.supplyChainSystem.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArotService {
    private final ArotRepository arotRepository;
    private final UserRepository userRepository;

    public List<Arot> getAllActive() {
        return arotRepository.findAllByDeletedAtIsNull();
    }

    public Arot getById(Long id) {
        return arotRepository.findById(id)
                .filter(a -> a.getDeletedAt() == null)
                .orElseThrow(() -> new RuntimeException("Arot not found"));
    }

    public Arot create(Arot arot, String email) {
        if (arotRepository.existsByPhoneAndDeletedAtIsNull(arot.getPhone())) {
            throw new RuntimeException("Arot with this phone already exists!");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));

        arot.setCreatedBy(String.valueOf(user.getId()));
        return arotRepository.save(arot);
    }

    public Arot updateArot(Long id, Arot updatedData) {
        Arot existing = getById(id);
        existing.setName(updatedData.getName());
        existing.setPhone(updatedData.getPhone());
        existing.setAddress(updatedData.getAddress());
        return arotRepository.save(existing);
    }

    public Arot updateStatus(Long id, String status) {
        Arot arot = getById(id);
        arot.setStatus(status);
        return arotRepository.save(arot);
    }

    public void delete(Long id) {
        Arot arot = getById(id);
        arot.setDeletedAt(LocalDateTime.now());
        arotRepository.save(arot);
    }

    public List<Arot> getTrash() {
        return arotRepository.findAllByDeletedAtIsNotNull();
    }
}
