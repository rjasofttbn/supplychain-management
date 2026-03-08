package com.example.supplyChainSystem.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // Save a value in Redis with TTL in seconds
    public void saveValue(String key, Object value, long timeoutSeconds) {
        redisTemplate.opsForValue().set(key, value, timeoutSeconds, TimeUnit.SECONDS);
        System.out.println(">>> Saved in Redis: " + key);
    }

    // Get a value from Redis
    public Object getValue(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        System.out.println(">>> Read from Redis: " + key + " -> " + (value != null ? "found" : "null"));
        return value;
    }

    // Delete a value from Redis
    public void deleteValue(String key) {
        redisTemplate.delete(key);
        System.out.println(">>> Deleted from Redis: " + key);
    }
}