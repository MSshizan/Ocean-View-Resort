/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Api.ApiConfig;
import Api.ApiResponse;
import Api.HttpClientWithJwt;
import DTO.EmailRequestDTO;
import DTO.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


/**
 * UserService is a singleton class responsible for managing user-related operations
 * via HTTP requests to the backend.
 *
 * It provides methods to get all users, find a user by email, approve or disapprove users,
 * and fetch security questions for password recovery.
 * All requests use HttpClientWithJwt to automatically attach JWT tokens for authorization.
 */
public class UserService {

    // Singleton instance
    private static UserService instance;

    // Base URL for user-related API endpoints
    private static final String BASE_URL = ApiConfig.BASE_URL + "/users";

    // HTTP client with automatic JWT support
    private final HttpClientWithJwt client = new HttpClientWithJwt();

    // Private constructor for singleton pattern
    private UserService() {}

    /**
     * Returns the singleton instance of UserService.
     */
    public static UserService getInstance() {
        if (instance == null) instance = new UserService();
        return instance;
    }

    // ===== GET ALL USERS =====
    /**
     * Fetches all users from the backend.
     *
     * @return List of UserDTOs; returns empty list if request fails
     */
    public List<UserDTO> getAllUsers() {
        try {
            ApiResponse<UserDTO[]> response = client.get(BASE_URL + "/all", UserDTO[].class);
            if (response.getStatus() == 200 && response.getData() != null) {
                return Arrays.asList(response.getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of(); // Return empty list if request fails
    }

    // ===== FIND USER BY EMAIL =====
    /**
     * Fetches a user by their email address.
     *
     * @param email User email
     * @return UserDTO if found; null if user does not exist or request fails
     */
    public UserDTO findUserByEmail(String email) {
        try {
            ApiResponse<UserDTO> response = client.get(BASE_URL + "/email/" + email, UserDTO.class);
            if (response.getStatus() == 200) return response.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Return null if user not found or request fails
    }

    // ===== APPROVE USER =====
    /**
     * Approves a user account by email.
     *
     * @param email User email
     * @return true if the operation succeeds; false otherwise
     */
    public boolean approveUser(String email) {
        try {
            ApiResponse<String> response = client.post(BASE_URL + "/approve", new EmailRequest(email), String.class);
            return response.getStatus() == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== DISAPPROVE USER =====
    /**
     * Disapproves a user account by email.
     *
     * @param email User email
     * @return true if the operation succeeds; false otherwise
     */
    public boolean disapproveUser(String email) {
        try {
            ApiResponse<String> response = client.post(BASE_URL + "/disapprove", new EmailRequest(email), String.class);
            return response.getStatus() == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== GET SECURITY QUESTION =====
    /**
     * Fetches the security question for a given email, used for password recovery.
     *
     * @param email User email
     * @return Security question string if found; null if not found or request fails
     */
    public String getSecurityQuestion(String email) {
        try {
            ApiResponse<String> response = client.get(BASE_URL + "/forget-email/" + email, String.class);
            if (response.getStatus() == 200) return response.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Return null if not found or request fails
    }

    // ===== PRIVATE HELPER CLASS =====
    /**
     * EmailRequest is a helper DTO used to send a user's email in POST requests
     * for approve/disapprove operations.
     */
    private static class EmailRequest {
        private String email;

        public EmailRequest(String email) { this.email = email; }

        public String getEmail() { return email; }

        public void setEmail(String email) { this.email = email; }
    }
}