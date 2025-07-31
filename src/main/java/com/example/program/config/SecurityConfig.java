package com.example.program.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
/*
this class will likely be needed later
for now i intend to disable some things and allow access
so i can test the endpoint i setup with the few example data pieces
 */

// @Configuration tells Spring Boot this is a setup class for custom settings
@Configuration
public class SecurityConfig {

    // Define custom security rules using Spring's DSL (Domain Specific Language)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 1️⃣ Disables CSRF (cross-site request forgery protection)
                .cors(Customizer.withDefaults()) // Enable CORS with default config
                .authorizeHttpRequests(auth -> auth // 2️⃣ Configure access rules
                        .requestMatchers("/api/public/**").permitAll() // 3️⃣ Allow all public paths
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // 4️⃣ Admin-only section
                        .requestMatchers("/api/staff/**").hasAnyRole("STAFF") // 5️⃣ Staff-level access
                        .anyRequest().authenticated() // 6️⃣ All other requests require login
                );

        return http.build(); // 7️⃣ Finalize the filter chain
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:3000",                  // local React dev server
                "https://your-frontend.vercel.app",       // deployed frontend (e.g., Vercel)
                "https://your-backend.onrender.com"       // optional, in case you test from browser directly
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true); // Important for cookies/token headers if needed

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
