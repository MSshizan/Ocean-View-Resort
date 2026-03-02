package org.example.ovr.service;




import org.example.ovr.Entity.User;
import org.example.ovr.dto.UserDTO;
import org.example.ovr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;




@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ===== Helper: Normalize Role =====
    private String normalizeRole(String role, boolean isAdminCreating) {
        if (role == null || role.isBlank()) {
            return isAdminCreating ? "ROLE_ADMIN" : "ROLE_USER";
        }
        role = role.trim().toUpperCase();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }
        return role;
    }

    // ===== SIGNUP =====
    public User addUser(UserDTO dto, boolean isAdminCreating) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail().trim().toLowerCase());

        // Encode once
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setSecurityQuestion(dto.getSecurityQuestion());
        user.setSecurityAnswer(dto.getSecurityAnswer());
        user.setAddress(dto.getAddress());
        user.setPhoneNumber(dto.getPhoneNumber());

        // Normalize role
        user.setRole(normalizeRole(dto.getRole(), isAdminCreating));

        user.setStatus(isAdminCreating ? dto.getStatus() : "inactive");

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase());
    }

    public boolean canLogin(User user, String rawPassword) {
        boolean passwordMatches = passwordEncoder.matches(rawPassword.trim(), user.getPassword());
        boolean statusActive = "active".equalsIgnoreCase(user.getStatus());

        System.out.println("Login attempt: " + user.getEmail()
                + " | passwordMatches=" + passwordMatches
                + " | statusActive=" + statusActive);

        return passwordMatches && statusActive;
    }

    public User updateUser(int id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail().trim().toLowerCase());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {

            if (!dto.getPassword().startsWith("$2a$")) { // BCrypt hash
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            } else {
                user.setPassword(dto.getPassword());
            }
        }

        user.setSecurityQuestion(dto.getSecurityQuestion());
        user.setSecurityAnswer(dto.getSecurityAnswer());
        user.setAddress(dto.getAddress());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setStatus(dto.getStatus());

        if (dto.getRole() != null && !dto.getRole().isBlank()) {
            user.setRole(normalizeRole(dto.getRole(), false));
        }

        return userRepository.save(user);
    }



    // ===== New helper: save user directly without touching password =====
    public User saveUserDirectly(User user) {
        return userRepository.save(user);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}