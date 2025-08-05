package com.example.program.service;

import com.example.program.model.User;
import com.example.program.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Used to fetch users by username

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Search for user in the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Return Spring Security user object with username, password, and roles
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), // login identifier
                user.getPassword(), // hashed password
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}