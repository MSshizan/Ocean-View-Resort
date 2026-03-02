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
 * AuthManager wraps AuthService and provides higher-level login/signup/forgotPassword methods.
 */
public class AuthManager {

    private final AuthService authService = new AuthService();

    // ===== LOGIN =====
    public LoginResponseDTO login(String email, String password) {
        try {
            System.out.println("[DEBUG] Attempting login with email: '" + email + "'");

            LoginDTO dto = new LoginDTO(email, password);
            ApiResponse<LoginResponseDTO> response = authService.login(dto);

            if (response.getData() != null) {
                System.out.println("[DEBUG] Login Response: Name=" + response.getData().getName() +
                        ", Role=" + response.getData().getRole() +
                        ", Token=" + response.getData().getToken());
            }

            if (response.getStatus() == 200 && response.getData() != null) {
                AuthContext.getInstance().setJwtToken(response.getData().getToken());
                return response.getData();
            }
        } catch (Exception e) {
            // Already handled in AuthService dialog
            return null;
        }
        return null;
    }

    // ===== SIGNUP =====
    public String signup(UserDTO user) {
        try {
            System.out.println("[DEBUG] Signup with email: " + user.getEmail());
            ApiResponse<String> response = authService.signup(user);
            return response.getMessage();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // ===== FORGOT PASSWORD =====
    public String forgotPassword(String email, String question, String answer, String newPassword) {
        try {
            ForgotPasswordDTO dto = new ForgotPasswordDTO(email, question, answer, newPassword);
            ApiResponse<String> response = authService.forgotPassword(dto);
            return response.getMessage();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}