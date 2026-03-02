package org.example.ovr.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.example.ovr.Entity.User;

public class UserDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Security question is required")
    private String securityQuestion;

    @NotBlank(message = "Security answer is required")
    private String securityAnswer;

    private String address;

    private String phoneNumber;

    private String status; // active, inactive

    private String role; // admin, staff, etc.
    private String createdAt;





    public UserDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.securityQuestion = user.getSecurityQuestion();
        this.securityAnswer = user.getSecurityAnswer();
        this.address = user.getAddress();
        this.phoneNumber = user.getPhoneNumber();
        this.status = user.getStatus();
        this.role = user.getRole();
        if (user.getCreatedAt() != null)
            this.createdAt = user.getCreatedAt().toString(); // or format as needed

    }
    public UserDTO() {}

    public UserDTO(String name, String email, String role, String status,String createdAt) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }



    // ===== GETTERS & SETTERS =====
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getSecurityQuestion() { return securityQuestion; }
    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }

    public String getSecurityAnswer() { return securityAnswer; }
    public void setSecurityAnswer(String securityAnswer) { this.securityAnswer = securityAnswer; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getCreatedAt() { return createdAt; }   // <-- ADD GETTER
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}

