
package Api;



import GUI.newlogin;

import java.awt.Frame;

import java.io.*;
import java.net.ConnectException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;


/**
 * HttpClientWithJwt is a helper class to make HTTP requests (GET, POST, PUT, DELETE)
 * to the backend while automatically attaching JWT tokens for authorization.
 * It also handles session expiration and JSON serialization/deserialization.
 
public class HttpClientWithJwt {

    // ObjectMapper from Jackson for converting objects to/from JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor registers JavaTimeModule to support LocalDateTime in JSON
     * and disables writing dates as timestamps.
     
    public HttpClientWithJwt() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Prepares a HttpURLConnection for the given URL and HTTP method.
     * Automatically adds JWT token in the Authorization header if available.
     
    private HttpURLConnection prepareConnection(String urlString, String method) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");

        // Attach JWT token if available
        String token = AuthContext.getInstance().getJwtToken();
        if (token != null && !token.isBlank()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

        // Enable sending body for POST and PUT
        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
            conn.setDoOutput(true);
        }

        return conn;
    }

    /**
     * Handles unauthorized responses (HTTP 401) by clearing auth context,
     * showing a message dialog, closing open windows, and reopening login window.
     
    private void handleUnauthorized(String message) {
        AuthContext.getInstance().clear();

        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, message);

            // Close all open frames
            for (Frame frame : Frame.getFrames()) {
                frame.dispose();
            }

            // Open login window
            new newlogin().setVisible(true);
        });

        throw new RuntimeException("Unauthorized - Session expired");
    }

    /**
     * Reads the response InputStream and returns it as a String.
     
    private String readStream(InputStream is) throws IOException {
        if (is == null) return "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }

    /**
     * Parses JSON response into ApiResponse<T>.
     * Returns an error ApiResponse if parsing fails.
     
    private <T> ApiResponse<T> parseResponse(String responseBody, int status, Class<T> responseType) {
        try {
            return objectMapper.readValue(
                    responseBody,
                    objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, responseType)
            );
        } catch (Exception ex) {
            return new ApiResponse<>(status, responseBody, null);
        }
    }

    // ===== HTTP METHODS =====

    /**
     * Sends a GET request to the given URL and parses the response into ApiResponse<T>.
     
    public <T> ApiResponse<T> get(String urlString, Class<T> responseType) throws IOException {
        HttpURLConnection conn = prepareConnection(urlString, "GET");
        int status = conn.getResponseCode();

        if (status == 401) handleUnauthorized("Session expired. Please login again.");

        InputStream is = status >= 400 ? conn.getErrorStream() : conn.getInputStream();
        String responseBody = readStream(is);

        return parseResponse(responseBody, status, responseType);
    }

    /**
     * Sends a POST request with a JSON body to the given URL.
     
    public <T> ApiResponse<T> post(String urlString, Object body, Class<T> responseType) throws IOException {
        HttpURLConnection conn = prepareConnection(urlString, "POST");

        // Write request body if provided
        if (body != null) {
            String json = objectMapper.writeValueAsString(body);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }
        }

        int status = conn.getResponseCode();
        if (status == 401) handleUnauthorized("Session expired. Please login again.");

        InputStream is = status >= 400 ? conn.getErrorStream() : conn.getInputStream();
        String responseBody = readStream(is);

        return parseResponse(responseBody, status, responseType);
    }

    /**
     * Sends a PUT request with a JSON body to the given URL.
     
    public <T> ApiResponse<T> put(String urlString, Object body, Class<T> responseType) throws IOException {
        HttpURLConnection conn = prepareConnection(urlString, "PUT");

        if (body != null) {
            String json = objectMapper.writeValueAsString(body);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }
        }

        int status = conn.getResponseCode();
        if (status == 401) handleUnauthorized("Session expired. Please login again.");

        InputStream is = status >= 400 ? conn.getErrorStream() : conn.getInputStream();
        String responseBody = readStream(is);

        return parseResponse(responseBody, status, responseType);
    }

    /**
     * Sends a DELETE request to the given URL.
     
    public <T> ApiResponse<T> delete(String urlString, Class<T> responseType) throws IOException {
        HttpURLConnection conn = prepareConnection(urlString, "DELETE");
        int status = conn.getResponseCode();

        if (status == 401) handleUnauthorized("Session expired. Please login again.");

        InputStream is = status >= 400 ? conn.getErrorStream() : conn.getInputStream();
        String responseBody = readStream(is);

        return parseResponse(responseBody, status, responseType);
    }
}
*/

/**
 * HttpClientWithJwt handles all HTTP requests.
 * Automatically shows a message box if the server is down or unreachable.
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;


// HTTP client with JWT support, automatic dialogs, no console stack traces
public class HttpClientWithJwt {

    private final ObjectMapper objectMapper;

    public HttpClientWithJwt() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // Prepare HttpURLConnection with headers and JWT
    private HttpURLConnection prepareConnection(String urlString, String method) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");

        // Add JWT token if available
        String token = AuthContext.getInstance().getJwtToken();
        if (token != null && !token.isBlank()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
            conn.setDoOutput(true);
        }

        // Set timeouts
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        return conn;
    }

    // Read response from InputStream
    private String readStream(InputStream is) throws IOException {
        if (is == null) return "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }

    // Parse JSON response into ApiResponse<T>
    private <T> ApiResponse<T> parseResponse(String responseBody, int status, Class<T> responseType) {
        try {
            return objectMapper.readValue(
                    responseBody,
                    objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, responseType)
            );
        } catch (Exception ex) {
            // Return raw response if parsing fails
            return new ApiResponse<>(status, responseBody, null);
        }
    }

    // Show error dialog on EDT
    private void showErrorDialog(String message, String title) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE)
        );
    }

    // Global handler for all requests
    private <T> ApiResponse<T> handleRequest(CheckedRequest<T> request) {
        try {
            return request.execute();
        } catch (ConnectException | SocketTimeoutException ex) {
            showErrorDialog("Server is down or not reachable. Please try again later.", "Connection Error");
            return new ApiResponse<>(503, "Server is down", null);
        } catch (IOException ex) {
            showErrorDialog("I/O Error: " + ex.getMessage(), "Connection Error");
            return new ApiResponse<>(500, "I/O error", null);
        } catch (Exception ex) {
            showErrorDialog("Unexpected Error: " + ex.getMessage(), "Error");
            return new ApiResponse<>(500, "Unexpected error", null);
        }
    }

    // Functional interface for lambda requests
    @FunctionalInterface
    private interface CheckedRequest<T> {
        ApiResponse<T> execute() throws Exception;
    }

    // ===== HTTP METHODS =====
    public <T> ApiResponse<T> get(String urlString, Class<T> responseType) {
        return handleRequest(() -> {
            HttpURLConnection conn = prepareConnection(urlString, "GET");
            int status = conn.getResponseCode();
            InputStream is = status >= 400 ? conn.getErrorStream() : conn.getInputStream();
            String responseBody = readStream(is);
            return parseResponse(responseBody, status, responseType);
        });
    }

    public <T> ApiResponse<T> post(String urlString, Object body, Class<T> responseType) {
        return handleRequest(() -> {
            HttpURLConnection conn = prepareConnection(urlString, "POST");
            if (body != null) {
                String json = objectMapper.writeValueAsString(body);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(json.getBytes());
                    os.flush();
                }
            }
            int status = conn.getResponseCode();
            InputStream is = status >= 400 ? conn.getErrorStream() : conn.getInputStream();
            String responseBody = readStream(is);
            return parseResponse(responseBody, status, responseType);
        });
    }

    public <T> ApiResponse<T> put(String urlString, Object body, Class<T> responseType) {
        return handleRequest(() -> {
            HttpURLConnection conn = prepareConnection(urlString, "PUT");
            if (body != null) {
                String json = objectMapper.writeValueAsString(body);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(json.getBytes());
                    os.flush();
                }
            }
            int status = conn.getResponseCode();
            InputStream is = status >= 400 ? conn.getErrorStream() : conn.getInputStream();
            String responseBody = readStream(is);
            return parseResponse(responseBody, status, responseType);
        });
    }

    public <T> ApiResponse<T> delete(String urlString, Class<T> responseType) {
        return handleRequest(() -> {
            HttpURLConnection conn = prepareConnection(urlString, "DELETE");
            int status = conn.getResponseCode();
            InputStream is = status >= 400 ? conn.getErrorStream() : conn.getInputStream();
            String responseBody = readStream(is);
            return parseResponse(responseBody, status, responseType);
        });
    }
}