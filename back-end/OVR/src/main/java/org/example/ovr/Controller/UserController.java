package org.example.ovr.Controller;



import org.example.ovr.Entity.User;
import org.example.ovr.Exception.ApiResponse;
import org.example.ovr.dto.EmailRequest;
import org.example.ovr.dto.UserDTO;
import org.example.ovr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
// Marks this class as a REST controller so Spring can handle HTTP requests and return JSON responses
@RestController

// Base URL mapping for all endpoints in this controller
@RequestMapping("/api/users")
public class UserController {

    // Injects UserService to handle business logic related to users
    @Autowired
    private UserService userService;

    // ===== GET ALL USERS =====
    // Handles GET requests to fetch all users
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        // Convert User entities to UserDTOs for the response
        List<UserDTO> dtos = userService.getAllUsers()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());

        // Return success response with list of users
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", dtos));
    }

    // ===== APPROVE A USER =====
    // Sets a user's status to active
    @PostMapping("/approve")
    public ResponseEntity<ApiResponse<String>> approveUser(@RequestBody EmailRequest request) {
        // Find the user by email; throw error if not found
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Only update status directly to avoid double-hashing of password
        user.setStatus("active");
        userService.saveUserDirectly(user); // Saves user without re-hashing password

        return ResponseEntity.ok(new ApiResponse<>(200, "User approved", "Success"));
    }

    // ===== DISAPPROVE A USER =====
    // Sets a user's status to inactive
    @PostMapping("/disapprove")
    public ResponseEntity<ApiResponse<String>> disapproveUser(@RequestBody EmailRequest request) {
        // Find the user by email; throw error if not found
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update status to inactive directly
        user.setStatus("inactive");
        userService.saveUserDirectly(user); // Saves user without affecting password

        return ResponseEntity.ok(new ApiResponse<>(200, "User disapproved", "Success"));
    }

    // ===== GET USER BY EMAIL =====
    // Fetch a single user using their email
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok(
                        new ApiResponse<>(200, "Success", new UserDTO(user))
                ))
                .orElse(ResponseEntity.ok(
                        new ApiResponse<>(404, "User not found", null)
                ));
    }

    // ===== GET SECURITY QUESTION BY EMAIL =====
    // Used for password recovery workflows
    @GetMapping("/forget-email/{email}")
    public ResponseEntity<ApiResponse<String>> getSecurityQuestion(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok(
                        new ApiResponse<>(200, "Success", user.getSecurityQuestion())
                ))
                .orElse(ResponseEntity.ok(
                        new ApiResponse<>(404, "User not found", null)
                ));
    }
}