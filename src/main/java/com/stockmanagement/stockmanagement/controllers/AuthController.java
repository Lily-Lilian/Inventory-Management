package com.stockmanagement.stockmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stockmanagement.stockmanagement.services.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        String role = authService.validateLogin(username, password);
        Map<String, Object> response = new HashMap<>();
        if (!role.isEmpty()) {
            response.put("status", "success");
            response.put("role", role);
            response.put("username", username);
        } else {
            response.put("status", "error");
            response.put("message", "Invalid credentials");
        }
        return ResponseEntity.ok(response);
    }

    // Signup endpoint
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String role = request.get("role");

        Map<String, Object> response = new HashMap<>();
        try {
            authService.addUser(username, password, role);
            response.put("status", "success");
            response.put("message", "User registered successfully");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
