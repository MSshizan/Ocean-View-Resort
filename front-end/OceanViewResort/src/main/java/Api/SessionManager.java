/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Api;
/**
 * SessionManager is a simple utility class to manage user session data on the client side.
 * It stores the JWT token and user role in memory for the current application session.
 * 
 * Author: sznah
 */
public class SessionManager {

    // JWT token of the currently logged-in user
    private static String jwtToken;

    // Role of the currently logged-in user (e.g., ADMIN, USER)
    private static String role;

    /**
     * Sets the current session with JWT token and user role.
     * This should be called after a successful login.
     *
     * @param token JWT token from the server
     * @param userRole Role of the logged-in user
     */
    public static void setSession(String token, String userRole) {
        jwtToken = token;
        role = userRole;
    }

    /**
     * Returns the current JWT token.
     *
     * @return JWT token as String, or null if not logged in
     */
    public static String getToken() {
        return jwtToken;
    }

    /**
     * Returns the role of the currently logged-in user.
     *
     * @return Role as String, or null if not logged in
     */
    public static String getRole() {
        return role;
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return true if JWT token exists and is not blank, false otherwise
     */
    public static boolean isLoggedIn() {
        return jwtToken != null && !jwtToken.isBlank();
    }

    /**
     * Clears the current session.
     * This should be called on logout to remove token and role from memory.
     */
    public static void clearSession() {
        jwtToken = null;
        role = null;
    }
}