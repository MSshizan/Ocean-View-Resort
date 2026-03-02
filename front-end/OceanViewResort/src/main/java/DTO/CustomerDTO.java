/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class CustomerDTO {

    @JsonProperty("customerName")
    private String name;
    private String idType;
    private String idNumber;
    private String gender;
    private String country;
    @JsonProperty("roomNumber")
    private int roomNumber;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String phoneNumber;

    // ✅ Default constructor needed for Jackson
    public CustomerDTO() {}

    // Optional: full constructor
    public CustomerDTO(String idType, String idNumber, String name, String gender,
                       String country, int roomNumber, LocalDateTime checkIn, LocalDateTime checkOut, String phoneNumber) {
        this.idType = idType;
        this.idNumber = idNumber;
        this.name = name;
        this.gender = gender;
        this.country = country;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.phoneNumber = phoneNumber;
    }

    // ===== Getters & Setters =====
    public String getCustomerName() { return name; }
    public void setCustomerName(String name) { this.name = name; }

    public String getIdType() { return idType; }
    public void setIdType(String idType) { this.idType = idType; }

    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public int getRoomNumber() { return roomNumber; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }

    public LocalDateTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDateTime checkIn) { this.checkIn = checkIn; }

    public LocalDateTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDateTime checkOut) { this.checkOut = checkOut; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}