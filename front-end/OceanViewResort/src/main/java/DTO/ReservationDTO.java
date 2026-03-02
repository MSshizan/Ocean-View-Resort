/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
package DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class ReservationDTO {

    private Long id;

    @JsonProperty("customerName")
    private String customerName;

    private String idType;
    private String idNumber;
    private String gender;
    private String country;

    @JsonProperty("roomNumber")
    private int roomNumber;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String phoneNumber;

    public ReservationDTO() {}
    
     public ReservationDTO(String idType, String idNumber, String customerName, String gender,
                       String country, int roomNumber, LocalDateTime checkIn, LocalDateTime checkOut, String phoneNumber) {
        this.idType = idType;
        this.idNumber = idNumber;
        this.customerName = customerName;
        this.gender = gender;
        this.country = country;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.phoneNumber = phoneNumber;
    }

    // Getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

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
*/

package DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;


public class ReservationDTO {

    private Long id;

    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("idType")
    private String idType;

    @JsonProperty("idNumber")
    private String idNumber;

    private String gender;
    private String country;

    // ✅ Use Integer so null is preserved
    @JsonProperty("roomNumber") // adjust if backend sends "room_id"
    private Integer roomNumber;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String phoneNumber;

    // ✅ New field for Bill PDF URL
    private String billPdfUrl;
    
    private String status;

    public ReservationDTO() {}

    public ReservationDTO(String idType, String idNumber, String customerName, String gender,
                          String country, Integer roomNumber, LocalDateTime checkIn,
                          LocalDateTime checkOut, String phoneNumber, String status) {
        this.idType = idType;
        this.idNumber = idNumber;
        this.customerName = customerName;
        this.gender = gender;
        this.country = country;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getIdType() { return idType; }
    public void setIdType(String idType) { this.idType = idType; }

    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Integer getRoomNumber() { return roomNumber; }
    public void setRoomNumber(Integer roomNumber) { this.roomNumber = roomNumber; }

    public LocalDateTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDateTime checkIn) { this.checkIn = checkIn; }

    public LocalDateTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDateTime checkOut) { this.checkOut = checkOut; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getBillPdfUrl() { return billPdfUrl; }
    public void setBillPdfUrl(String billPdfUrl) { this.billPdfUrl = billPdfUrl; }
    
     public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}