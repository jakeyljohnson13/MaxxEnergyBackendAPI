package com.example.program.config;


import com.example.program.model.Role;
import com.example.program.model.User;
import com.example.program.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    private final UserRepository users;

    public JwtAuthFilter(JwtService jwt, UserRepository users) {
        this.jwt = jwt; this.users = users;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String header = req.getHeader("Authorization");

        // Only handle Bearer tokens; otherwise continue the chain
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        String token = header.substring(7); // chop off "Bearer "

        try {
            // 1) pull identity & role from the token
            String username = jwt.extractUsername(token);     // subject set in generate(...)
            Role role = jwt.extractRole(token);               // claim "role" -> enum

            // 2) avoid overriding an existing auth (another filter or form-login)
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // (optional but recommended) re-hydrate user to ensure still valid/enabled
                Optional<User> maybeUser = users.findByUsername(username);
                if (maybeUser.isPresent()) {
                    // turn enum into "ROLE_X" authority
                    var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));

                    var auth = new UsernamePasswordAuthenticationToken(
                            username,       // principal (you could also use the User entity)
                            null,           // credentials (don’t put passwords here)
                            authorities
                    );
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

        } catch (ExpiredJwtException e) {
            // token known, but expired
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Token expired");
            return; // stop further processing
        } catch (JwtException e) {
            // malformed / signature invalid / etc. -> just continue unauthenticated
            // (alternatively, send 401 if you prefer hard-fail)
        }

        // Continue down the filter chain
        chain.doFilter(req, res);
    }
}
