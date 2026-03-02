package org.example.ovr.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


// Indicates that this class contains Spring configuration for beans
@Configuration
public class AppConfig {

    // Defines a bean of type PasswordEncoder that can be injected wherever needed
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Returns a BCryptPasswordEncoder instance
        // BCrypt is a strong hashing function suitable for storing passwords securely
        return new BCryptPasswordEncoder();
    }
}