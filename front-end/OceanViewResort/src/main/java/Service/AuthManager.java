/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

// AuthManager.java

import Api.ApiResponse;
import Api.AuthContext;
import Api.AuthService;
import DTO.ForgotPasswordDTO;
import DTO.LoginDTO;
import DTO.LoginResponseDTO;
import DTO.UserDTO;
import java.io.IOException;



/**
 * AuthManager acts as a client-side helper to handle authentication operations.
 * It communicates with AuthService to perform login, signup, and forgot-password actions.
 * It also updates the AuthContext with JWT tokens for session management.
 */
public class AuthManager {

    // AuthService instance to call backend authentication endpoints
    private final AuthService authService = new AuthService();

    // ===== LOGIN =====
    /**
     * Performs login with email and password.
     * If login is successful, stores JWT token in AuthContext for session management.
     *
     * @param email User email
     * @param password User password
     * @return LoginResponseDTO containing user info and token if successful; null otherwise
     */
    public LoginResponseDTO login(String email, String password) {
        try {
            System.out.println("[DEBUG] Attempting login with email: '" + email + "' and password: '" + password + "'");

            // Prepare login request DTO
            LoginDTO dto = new LoginDTO(email, password);

            // Call backend login endpoint
            ApiResponse<LoginResponseDTO> response = authService.login(dto);
            System.out.println("[DEBUG] Login Response Status: " + response.getStatus());

            // Log response data for debugging
            if (response.getData() != null) {
                System.out.println("[DEBUG] Login Response Data: Name=" + response.getData().getName() +
                        ", Role=" + response.getData().getRole() +
                        ", Token=" + response.getData().getToken());
            } else {
                System.out.println("[DEBUG] Login failed: response data is null");
            }

            // If login successful, save JWT token globally in AuthContext
            if (response.getStatus() == 200) {
                AuthContext.getInstance().setJwtToken(response.getData().getToken());
                return response.getData();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log exceptions
        }
        return null; // Return null if login fails
    }

    // ===== SIGNUP =====
    /**
     * Performs user signup by sending user details to the backend.
     *
     * @param user UserDTO containing registration info
     * @return Response message from the server
     */
    public String signup(UserDTO user) {
        try {
            System.out.println("[DEBUG] Signup with email: " + user.getEmail());

            // Call backend signup endpoint
            ApiResponse<String> response = authService.signup(user);
            System.out.println("[DEBUG] Signup Response: " + response.getMessage());
            return response.getMessage();
        } catch (Exception e) {
            e.printStackTrace(); // Log exceptions
            return "Error: " + e.getMessage();
        }
    }

    // ===== FORGOT PASSWORD =====
    /**
     * Performs forgot-password operation by verifying security question and updating password.
     *
     * @param email User email
     * @param question Security question
     * @param answer Answer to the security question
     * @param newPassword New password to set
     * @return Response message from the server
     */
    public String forgotPassword(String email, String question, String answer, String newPassword) {
        try {
            System.out.println("[DEBUG] Forgot Password for email: " + email);

            // Prepare forgot-password DTO
            ForgotPasswordDTO dto = new ForgotPasswordDTO(email, question, answer, newPassword);

            // Call backend forgot-password endpoint
            ApiResponse<String> response = authService.forgotPassword(dto);
            System.out.println("[DEBUG] Forgot Password Response: " + response.getMessage());
            return response.getMessage();
        } catch (Exception e) {
            e.printStackTrace(); // Log exceptions
            return "Error: " + e.getMessage();
        }
    }
}