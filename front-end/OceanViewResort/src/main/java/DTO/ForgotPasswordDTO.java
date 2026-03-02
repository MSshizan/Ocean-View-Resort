/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

public class ForgotPasswordDTO {
    private String email;
    private String securityQuestion;
    private String securityAnswer;
    private String newPassword;

    public ForgotPasswordDTO() {}

    public ForgotPasswordDTO(String email, String question, String answer, String newPassword) {
        this.email = email;
        this.securityQuestion = question;
        this.securityAnswer = answer;
        this.newPassword = newPassword;
    }

    // Getters & Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSecurityQuestion() { return securityQuestion; }
    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }

    public String getSecurityAnswer() { return securityAnswer; }
    public void setSecurityAnswer(String securityAnswer) { this.securityAnswer = securityAnswer; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
