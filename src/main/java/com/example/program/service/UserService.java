package com.example.program.service;


import com.example.program.dto.RegisterRequest;
import com.example.program.model.Role;
import com.example.program.model.User;
import com.example.program.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public UserService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Transactional
    public void register(RegisterRequest req) {
        // Uniqueness checks (fast failures)
        if (users.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (users.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        // Map DTO -> Entity
        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setRole(Role.USER); // default

        users.save(u);
    }

}
