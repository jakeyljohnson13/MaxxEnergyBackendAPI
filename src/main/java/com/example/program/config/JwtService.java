package com.example.program.config;

import com.example.program.model.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;


//@Service
public class JwtService {
    private final Key key;
    private final long expMs;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.exp-min}") long expMin) {

        System.out.println("🔧 Constructor: secret=" + secret);
        System.out.println("🔧 Constructor: expMin=" + expMin);
        System.out.println("🔧 ENV JWT_SECRET=" + System.getenv("JWT_SECRET"));
        System.out.println("🔧 ENV JWT_EXP_MIN=" + System.getenv("JWT_EXP_MIN"));


        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("app.jwt.secret must be at least 32 characters");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expMs = expMin * 60_000;
    }
    @PostConstruct
    public void init() {
        System.out.println("JWT_SECRET: " + System.getenv("JWT_SECRET"));
        System.out.println("JWT_EXP_MIN: " + System.getenv("JWT_EXP_MIN"));
    }


    public String generate(String username, Role role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .setExpiration(new Date(System.currentTimeMillis() + expMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public Role extractRole(String token) {
        Claims claims = parse(token).getBody();
        String roleStr = claims.get("role", String.class);
        return Role.valueOf(roleStr); // converts "ADMIN" -> Role.ADMIN
    }

    public String extractUsername(String token) {
        return parse(token).getBody().getSubject();
    }


}
