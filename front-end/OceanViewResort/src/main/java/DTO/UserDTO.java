/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;


import java.time.LocalDateTime;

public class UserDTO {

    private String name;
    private String email;
    private String password;
    private String securityQuestion;
    private String securityAnswer;
    private String address;
    private String phoneNumber;
    private String status; // active / inactive
    private String role;   // admin / user
    private String createdAt;
    public UserDTO() {
    }
    
    public UserDTO(String name, String email, String password,
                   String question, String answer,
                   String address, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.securityQuestion = question;
        this.securityAnswer = answer;
        this.address = address;
        this.phoneNumber = phone;
        
    }

    // Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }

    public void String(String createdAt) {
        this.createdAt = createdAt;
    }
}
