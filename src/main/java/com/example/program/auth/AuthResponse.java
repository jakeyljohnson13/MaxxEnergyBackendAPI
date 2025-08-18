package com.example.program.auth;


import com.example.program.model.Role;

public record AuthResponse(
        String token,
        String username,
        Role role
) {}
