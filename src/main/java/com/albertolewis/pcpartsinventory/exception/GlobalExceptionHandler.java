package com.albertolewis.pcpartsinventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Invalid input");
        error.put("message", ex.getMessage());
        error.put("status", 400);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(PcPartNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePcPartNotFoundException(PcPartNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Not Found");
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException() {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Unauthorized");
        error.put("message", "Invalid username or password");
        error.put("status", HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

}
