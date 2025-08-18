package com.example.program.config;

import com.example.program.model.User;
import com.example.program.model.Role;
import com.example.program.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppStartupConfig {

    @Bean
    public CommandLineRunner createInitialUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            // Examples don't actually add as they are unsecure. used for testing and removed from database already

            /*
            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123")); // 🔐 hashed
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("✔ Admin created.");
            }

            if (userRepository.findByEmail("staff@example.com").isEmpty()) {
                User staff = new User();
                staff.setUsername("staff");
                staff.setEmail("staff@example.com");
                staff.setPassword(passwordEncoder.encode("staff123"));
                staff.setRole(Role.STAFF);
                userRepository.save(staff);
                System.out.println("✔ Staff created.");
            }
*/

        };
    }
}