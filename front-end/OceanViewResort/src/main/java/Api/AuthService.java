/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Api;

/**
 *
 * @author sznah
 */
import DTO.ForgotPasswordDTO;
import DTO.LoginDTO;
import DTO.LoginResponseDTO;
import DTO.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * AuthService handles communication with the backend authentication API.
 * It provides methods for login, signup, and forgot-password functionality.
 * All requests are sent as HTTP POST requests with JSON payloads.
 */
public class AuthService {

    // Base URL for all authentication-related API endpoints
    private static final String BASE_URL = ApiConfig.BASE_URL + "/auth";

    // ObjectMapper from Jackson library for converting objects to/from JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Sends a login request to the backend.
     * @param request LoginDTO containing email/password
     * @return ApiResponse containing LoginResponseDTO on success
     * @throws IOException if network or parsing fails
     */
    public ApiResponse<LoginResponseDTO> login(LoginDTO request) throws IOException {
        return post("/login", request, LoginResponseDTO.class);
    }

    /**
     * Sends a signup request to the backend.
     * @param request UserDTO containing user registration info
     * @return ApiResponse containing a message string
     * @throws IOException if network or parsing fails
     */
    public ApiResponse<String> signup(UserDTO request) throws IOException {
        return post("/signup", request, String.class);
    }

    /**
     * Sends a forgot-password request to the backend.
     * @param request ForgotPasswordDTO containing email and security info
     * @return ApiResponse containing a message string
     * @throws IOException if network or parsing fails
     */
    public ApiResponse<String> forgotPassword(ForgotPasswordDTO request) throws IOException {
        return post("/forgot-password", request, String.class);
    }

    /**
     * Generic helper method to send POST requests to the backend.
     * Converts the request object to JSON and parses the JSON response into ApiResponse<T>.
     *
     * @param endpoint      API endpoint path (e.g., "/login")
     * @param requestObject Object to send as JSON body
     * @param responseType  Class type of the response payload
     * @param <T>           Type of the data in ApiResponse
     * @return ApiResponse containing the parsed response data
     * @throws IOException if network or parsing fails
     */
    private <T> ApiResponse<T> post(String endpoint, Object requestObject, Class<T> responseType) throws IOException {
        // Construct full URL
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Configure connection for POST and JSON content
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true); // Enable sending a request body

        // Convert request object to JSON string
        String jsonInput = objectMapper.writeValueAsString(requestObject);

        // Write JSON payload to request body
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes());
        }

        // Choose input stream based on response code (error or success)
        InputStream is = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();

        // Parse JSON response into ApiResponse<T> and return
        return objectMapper.readValue(
                is,
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, responseType)
        );
    }
}