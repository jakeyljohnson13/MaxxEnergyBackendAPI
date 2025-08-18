package com.example.program.config;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
    // read from application.properties: app.cors.allowed-origin=http://localhost:5173
    @Value("${app.cors.allowed-origin:http://localhost:5173}")
    private String allowedOrigin;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authManager(AuthenticationConfiguration c) throws Exception {
        return c.getAuthenticationManager();
    }

    private final CorsConfigurationSource corsSource;
    private final JwtAuthFilter jwtFilter; // your JWT filter bean

    public SecurityConfig(CorsConfigurationSource corsSource, JwtAuthFilter jwtFilter) {
        this.corsSource = corsSource;
        this.jwtFilter = jwtFilter;
    }

    private CorsConfigurationSource corsSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(allowedOrigin));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization","Content-Type","Accept","X-Requested-With","Origin"));
        cfg.setExposedHeaders(List.of("Authorization")); // optional, useful if you ever return tokens in headers
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }

    // Define custom security rules using Spring's DSL (Domain Specific Language)
    // Declares a Spring-managed bean to configure the security filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 🔒 CSRF (Cross-Site Request Forgery) protection is disabled here
        // In REST APIs (especially token-based ones like JWT), CSRF isn't typically needed
       http.csrf(csrf -> csrf.disable());
        //--------------------------------------------------------------------------------------------------
        // 🌍 Enable Cross-Origin Resource Sharing (CORS)
        // Allows your frontend (on a different domain/port) to make requests to this backend
        // Customizer.withDefaults() applies Spring Boot's default CORS config
        http.cors(cors -> cors.configurationSource(corsSource())); // Enable CORS with default config
        //--------------------------------------------------------------------------------------------------
        // ⚙️ Manage how sessions are handled in the app
        http.sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //--------------------------------------------------------------------------------------------------

        // ✅ Define access rules for different routes based on user roles
        http.authorizeHttpRequests(auth -> auth
                // Permit the login page itself (GET /login)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Allow registering and logging in a user (POST /auth/register and /auth/login)
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                .requestMatchers("/api/public/**").permitAll()

                // Role-guarded areas
                .requestMatchers("/user/**").hasAnyRole("USER","STAFF","ADMIN")
                .requestMatchers("/staff/**").hasAnyRole("STAFF","ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // default for the rest of the API
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
        );
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);
        //--------------------------------------------------------------------------------------------------

         // return 401/403 as JSON/errors (no redirects)
        http.exceptionHandling(e -> e
                .authenticationEntryPoint((req, res, ex) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.getWriter().write("Unauthorized");
                })
                .accessDeniedHandler((req, res, ex) -> {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.getWriter().write("Forbidden");
                })
        );

// put your JWT filter before username/password auth
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // ✅ Build and return the configured HttpSecurity object as a filter chain
        return http.build();
    }

    //--------------------------------------------------------------------------------------------------



}
