/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.Map;
import java.util.prefs.Preferences;



/**
 * AuthContext is a singleton class that manages authentication state for the client.
 * It stores JWT token, user role, and token expiration information.
 * The data is persisted using Java Preferences API so it can survive app restarts.
 */
public class AuthContext {

    // Singleton instance
    private static AuthContext instance;

    // JWT token string
    private String jwtToken;

    // Token expiration time in milliseconds since epoch
    private long tokenExpirationMillis;

    // Role of the authenticated user
    private String role;

    // Preferences node for storing auth data persistently on the client machine
    private static final Preferences prefs =
            Preferences.userRoot().node(AuthContext.class.getName());

    // ObjectMapper for parsing JWT JSON payloads
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Private constructor for singleton pattern.
     * Loads saved auth data (JWT, role, expiration) from preferences if available.
     */
    private AuthContext() {
        jwtToken = prefs.get("jwtToken", null);
        role = prefs.get("role", null);
        tokenExpirationMillis = prefs.getLong("tokenExpirationMillis", 0);
    }

    /**
     * Returns the singleton instance of AuthContext.
     * Creates a new instance if it does not exist.
     */
    public static AuthContext getInstance() {
        if (instance == null) {
            instance = new AuthContext();
        }
        return instance;
    }

    /**
     * Sets a new JWT token and updates expiration time and persistent storage.
     */
    public void setJwtToken(String token) {
        this.jwtToken = token;
        this.tokenExpirationMillis = parseExpiryFromJwt(token); // extract expiry from JWT
        prefs.put("jwtToken", token); // persist token
        prefs.putLong("tokenExpirationMillis", tokenExpirationMillis); // persist expiry
    }

    // Returns the current JWT token
    public String getJwtToken() {
        return jwtToken;
    }

    /**
     * Sets the user role and persists it.
     */
    public void setRole(String role) {
        this.role = role;
        prefs.put("role", role);
    }

    // Returns the current user role
    public String getRole() {
        return role;
    }

    /**
     * Clears authentication state and removes saved preferences.
     * Use this on logout.
     */
    public void clear() {
        jwtToken = null;
        role = null;
        tokenExpirationMillis = 0;
        prefs.remove("jwtToken");
        prefs.remove("role");
        prefs.remove("tokenExpirationMillis");
    }

    /**
     * Checks if the JWT token exists and is still valid (not expired).
     */
    public boolean hasValidToken() {
        return jwtToken != null && !jwtToken.isBlank() &&
               System.currentTimeMillis() < tokenExpirationMillis;
    }

    /**
     * Parses the JWT token to extract the "exp" (expiry) claim in seconds
     * and converts it to milliseconds for easier comparison with System.currentTimeMillis().
     * Returns 0 if parsing fails.
     */
    private long parseExpiryFromJwt(String token) {
        try {
            String[] parts = token.split("\\."); // JWT format: header.payload.signature
            if (parts.length < 2) return 0;

            // Decode the payload (base64url) to JSON
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));

            // Convert JSON payload to Map
            Map<String, Object> payload = mapper.readValue(payloadJson, Map.class);

            // Extract "exp" claim if present and convert to milliseconds
            if (payload.containsKey("exp")) {
                long expSeconds = ((Number) payload.get("exp")).longValue();
                return expSeconds * 1000; // convert seconds to milliseconds
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log parsing errors
        }
        return 0; // Default if parsing fails
    }
}