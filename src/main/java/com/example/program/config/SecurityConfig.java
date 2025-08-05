package com.example.program.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    // Declares a Spring-managed bean to configure the security filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 🔒 CSRF (Cross-Site Request Forgery) protection is disabled here
        // In REST APIs (especially token-based ones like JWT), CSRF isn't typically needed
        // Enable only if you're using form-based login (e.g., with Thymeleaf UI)
        http.csrf(csrf -> csrf.disable());// Disables CSRF (cross-site request forgery protection)
        //--------------------------------------------------------------------------------------------------
        // 🌍 Enable Cross-Origin Resource Sharing (CORS)
        // Allows your frontend (on a different domain/port) to make requests to this backend
        // Customizer.withDefaults() applies Spring Boot's default CORS config
        http.cors(Customizer.withDefaults()); // Enable CORS with default config
        //--------------------------------------------------------------------------------------------------
        // ⚙️ Manage how sessions are handled in the app
        http.sessionManagement(session -> session
                // 🧾 Stateless means Spring Security won't use sessions to store user state
                // Ideal for APIs using tokens like JWT instead of session cookies
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        //--------------------------------------------------------------------------------------------------

        // ✅ Define access rules for different routes based on user roles
        http.authorizeHttpRequests(auth -> auth
                // 🟢 Public APIs - no login required
                .requestMatchers("/api/public/**").permitAll()

                // 🟡 Logged-in users with any of these roles can access /user/** endpoints
                .requestMatchers("/user/**").hasAnyRole("USER", "STAFF", "ADMIN")

                // 🔵 Only STAFF and ADMIN can access /staff/** endpoints
                .requestMatchers("/staff/**").hasAnyRole("STAFF", "ADMIN")

                // 🔴 Only ADMINs can access /admin/** endpoints
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // 🔐 All other endpoints require login/authentication
                .anyRequest().authenticated()
        );

        // 🚪 Configure logout behavior
        http.logout(logout -> logout
                // 🔗 The endpoint that triggers logout (must be called via POST unless CSRF disabled)
                .logoutUrl("/logout")

                // ✅ Where the user should be redirected after successful logout
                .logoutSuccessUrl("/auth/logout-success")

                // 💣 Invalidate session data (mostly redundant for stateless but good practice)
                .invalidateHttpSession(true)

                // 🧼 Deletes session cookie so browser doesn't try to reuse old session
                .deleteCookies("JSESSIONID")
        );

        // 🚨 Define custom behavior when access is denied (e.g., not enough permissions)
        http.exceptionHandling(eh -> eh
                .accessDeniedHandler((req, res, ex) -> {
                    // 🔒 Return 403 Forbidden if user doesn't have the correct role
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);

                    // 📝 Respond with a clear error message
                    res.getWriter().write("Access Denied: You do not have permission.");
                })
        );

        // ✅ Build and return the configured HttpSecurity object as a filter chain
        return http.build();
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
