package com.example.supplyChainSystem.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice; // Import this

@RestControllerAdvice // <--- THIS IS THE KEY
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        // Now Postman will show exactly: "An item with this name already exists."
        // And the Status Code will be 400 Bad Request
        return ResponseEntity.status(400).body(ex.getMessage());
    }
}