package com.example.program.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/*
this class will likely be needed later
for now i intend to disable some things and allow access
so i can test the endpoint i setup with the few example data pieces
 */

// @Configuration tells Spring Boot this is a setup class for custom settings
@Configuration
// @EnableWebSecurity turns on Spring Security (but we override it below)
@EnableWebSecurity
public class SecurityConfig {

    // Define custom security rules using Spring's DSL (Domain Specific Language)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection (fine for testing, unsafe in production)
                .csrf(csrf -> csrf.disable())

                // Allow access to all requests without login or roles
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        // Finalize and return the custom security filter chain
        return http.build();
    }
}
