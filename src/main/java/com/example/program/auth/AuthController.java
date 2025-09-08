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

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.example.program.model.Role.USER;

@CrossOrigin(origins = "https://maxxenergy-vite-react.vercel.app")
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
        if (u == null) {
            return ResponseEntity.status(401).body("Username not found");
        }
        if (!encoder.matches(req.password(), u.getPassword())) {
            return ResponseEntity.status(401).body("Incorrect password");
        }
        var token = jwt.generate(u.getUsername(), u.getRole());
        return ResponseEntity.ok(new AuthResponse(token, u.getUsername(), u.getRole()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok("ok"); // already authenticated by filter; customize as needed
    }
    @GetMapping("/ping")
    public ResponseEntity<Map<String, Object>> ping() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "pong");
        response.put("timestamp", Instant.now());
        return ResponseEntity.ok(response);
    }


}
