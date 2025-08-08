package com.example.program.controllers;

import com.example.program.model.Role;
import com.example.program.model.User;
import com.example.program.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository; // Handles DB interaction for User

    @Autowired
    private PasswordEncoder passwordEncoder; // Securely hashes the password



    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already taken!";
        }

        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default role (can also come from the request)
        user.setRole(Role.USER);

        // Save user to database
        userRepository.save(user);

        return "User registered successfully!";
    }

}
