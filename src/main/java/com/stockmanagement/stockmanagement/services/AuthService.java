package com.stockmanagement.stockmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockmanagement.stockmanagement.models.User;
import com.stockmanagement.stockmanagement.repositories.UserRepository;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Validate login credentials
    public String validateLogin(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        return user.map(User::getRole).orElse("");
    }

    // Add a new user
    public void addUser(String username, String password, String role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole(role);
        userRepository.save(newUser);
    }
}

