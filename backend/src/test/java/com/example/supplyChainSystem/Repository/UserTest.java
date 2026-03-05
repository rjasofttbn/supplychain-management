package com.example.supplyChainSystem.Repository;

import com.example.supplyChainSystem.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void test(){
        System.out.println(userRepository.findByEmail("omar.faruk@gigatechltd97.com").get());
    }
}
