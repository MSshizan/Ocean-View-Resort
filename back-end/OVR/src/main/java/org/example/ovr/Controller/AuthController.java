package org.example.ovr.Controller;


import org.example.ovr.Entity.User;
import org.example.ovr.Exception.ApiResponse;
import org.example.ovr.Security.JwtProvider;
import org.example.ovr.dto.ForgotPasswordDTO;
import org.example.ovr.dto.LoginDTO;
import org.example.ovr.dto.UserDTO;
import org.example.ovr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// Marks this class as a REST controller, so Spring can handle HTTP requests and return JSON responses
@RestController

// Base URL mapping for all endpoints in this controller
@RequestMapping("/api/auth")
public class AuthController {

    // Injects the UserService to handle user-related operations
    @Autowired
    private UserService userService;

    // Injects JwtProvider to generate JSON Web Tokens for authentication
    @Autowired
    private JwtProvider jwtProvider;

    // ===== SIGNUP =====
    // Handles user registration requests
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(
            @RequestBody UserDTO userDTO) {  // Maps incoming JSON to UserDTO object

        // Check if the email is already registered
        if (userService.findByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400,
                            "Email already registered",
                            null));  // Return error response if email exists
        }

        // Add user to the database (inactive by default)
        userService.addUser(userDTO, false);

        // Return success response with CREATED status
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201,
                        "User registered successfully. Wait for admin approval.",
                        "Success"));
    }

    // ===== LOGIN =====
    // Handles user login requests
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(
            @RequestBody LoginDTO loginDTO) {  // Maps login JSON data to LoginDTO

        // Find user by email
        Optional<User> optionalUser =
                userService.findByEmail(loginDTO.getEmail());

        // If user not found, return UNAUTHORIZED
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(401,
                            "Invalid credentials",
                            null));
        }

        User user = optionalUser.get();

        // Check if password is correct and account is active
        if (!userService.canLogin(user, loginDTO.getPassword())) {

            // If account is not active, return FORBIDDEN
            if (!"active".equalsIgnoreCase(user.getStatus())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(403,
                                "Account inactive. Wait for admin approval.",
                                null));
            }

            // If credentials are wrong, return UNAUTHORIZED
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(401,
                            "Invalid credentials",
                            null));
        }

        // Generate JWT token for successful login
        String token = jwtProvider.generateToken(user);

        // Prepare response data
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        data.put("email", user.getEmail());
        data.put("role", user.getRole());
        data.put("name", user.getName());

        // Return success response with user info and token
        return ResponseEntity.ok(
                new ApiResponse<>(200,
                        "Login successful",
                        data)
        );
    }

    // ===== FORGOT PASSWORD =====
    // Handles requests to reset password using security question
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody ForgotPasswordDTO dto) {

        // Find user by email
        Optional<User> optionalUser = userService.findByEmail(dto.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "Email not found", null));
        }

        User user = optionalUser.get();

        // Verify security question and answer
        if (!user.getSecurityQuestion().equalsIgnoreCase(dto.getSecurityQuestion()) ||
                !user.getSecurityAnswer().equalsIgnoreCase(dto.getSecurityAnswer())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "Security question or answer is incorrect", null));
        }

        // Update password
        user.setPassword(dto.getNewPassword());
        userService.updateUser(user.getId(), new UserDTO(user));

        // Return success response
        return ResponseEntity.ok(new ApiResponse<>(200, "Password updated successfully", "Success"));
    }
}
