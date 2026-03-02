package org.example.ovr.dto;

public class ForgotPasswordDTO {
    private String email;
    private String securityQuestion;
    private String securityAnswer;
    private String newPassword;

    // getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSecurityQuestion() { return securityQuestion; }
    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }

    public String getSecurityAnswer() { return securityAnswer; }
    public void setSecurityAnswer(String securityAnswer) { this.securityAnswer = securityAnswer; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}