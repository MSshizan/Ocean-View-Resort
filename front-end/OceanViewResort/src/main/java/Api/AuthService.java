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
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;



/**
 * AuthService handles communication with the backend authentication API.
 * Uses manual HttpURLConnection but shows dialogs when the server is down.
 */
public class AuthService {

    private static final String BASE_URL = ApiConfig.BASE_URL + "/auth";
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ===== LOGIN =====
    public ApiResponse<LoginResponseDTO> login(LoginDTO request) {
        return sendPost("/login", request, LoginResponseDTO.class, false);
    }

    // ===== SIGNUP =====
    public ApiResponse<String> signup(UserDTO request) {
        return sendPost("/signup", request, String.class, true);
    }

    // ===== FORGOT PASSWORD =====
    public ApiResponse<String> forgotPassword(ForgotPasswordDTO request) {
        return sendPost("/forgot-password", request, String.class, true);
    }

    /**
     * Generic POST request helper
     *
     * @param endpoint API endpoint path
     * @param requestObject request body object
     * @param responseType expected response class
     * @param attachJwt whether to attach JWT from AuthContext
     * @param <T> response type
     * @return ApiResponse<T>
     */
    private <T> ApiResponse<T> sendPost(String endpoint, Object requestObject, Class<T> responseType, boolean attachJwt) {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Attach JWT if needed and available
            if (attachJwt) {
                String token = AuthContext.getInstance().getJwtToken();
                if (token != null && !token.isBlank()) {
                    conn.setRequestProperty("Authorization", "Bearer " + token);
                }
            }

            // Convert request object to JSON
            String jsonInput = objectMapper.writeValueAsString(requestObject);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            // Choose response stream
            InputStream is = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();
            return objectMapper.readValue(
                    is,
                    objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, responseType)
            );

        } catch (ConnectException | SocketTimeoutException ex) {
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null,
                            "Server is down or not reachable. Please try again later.",
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE)
            );
            return new ApiResponse<>(503, "Server is down", null);

        } catch (Exception ex) {
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null,
                            "Unexpected Error: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE)
            );
            return new ApiResponse<>(500, "Unexpected error", null);
        }
    }
}