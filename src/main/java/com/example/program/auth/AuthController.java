package com.example.program.auth;

import com.example.program.config.JwtService;
import com.example.program.dto.LoginRequest;
import com.example.program.dto.RegisterRequest;
import com.example.program.model.User;
import com.example.program.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.example.program.model.Role.USER;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(UserRepository repo, PasswordEncoder encoder, JwtService jwt) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (repo.existsByUsername(req.getUsername()))
            return ResponseEntity.badRequest().body("Username taken");

        if (repo.existsByEmail(req.getEmail()))
            return ResponseEntity.badRequest().body("Email taken");

        var u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPassword(encoder.encode(req.getPassword())); // BCrypt -> $2a$...
        u.setRole(USER);
        repo.save(u);

        var token = jwt.generate(u.getUsername(), u.getRole());
        return ResponseEntity.ok(new AuthResponse(token, u.getUsername(), u.getRole()));
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        var u = repo.findByUsername(req.username()).orElse(null);
        if (u == null || !encoder.matches(req.password(), u.getPassword()))
            return ResponseEntity.status(401).body("Invalid credentials");

        var token = jwt.generate(u.getUsername(), u.getRole());
        return ResponseEntity.ok(new AuthResponse(token, u.getUsername(), u.getRole()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok("ok"); // already authenticated by filter; customize as needed
    }

}
