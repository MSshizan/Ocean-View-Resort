/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Api;

/**
 *
 * @author sznah
 */



public class ApiResponse<T> {
    private int status;       // HTTP-like status code
    private String message;   // Success or error message
    private T data;           // Generic data (could be UserDTO, String, etc.)

    public ApiResponse() {}

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Getters & Setters
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}