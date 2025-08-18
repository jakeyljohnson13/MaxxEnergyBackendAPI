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
        String h = req.getHeader("Authorization");
        if (h == null || !h.startsWith("Bearer ")) {
            // No token => do NOT fail; let permitAll rules apply
            chain.doFilter(req, res);
            return;
        }

        try {
            var claims = jwt.parse(h.substring(7)).getBody();
            String username = claims.getSubject();
            String roleStr  = claims.get("role", String.class); // if you store the enum name
            var auth = new UsernamePasswordAuthenticationToken(
                    username, null, List.of(new SimpleGrantedAuthority("ROLE_" + roleStr))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception ignored) {
            // Token bad? Don’t nuke open endpoints—just continue.
        }
        chain.doFilter(req, res);
    }
}
