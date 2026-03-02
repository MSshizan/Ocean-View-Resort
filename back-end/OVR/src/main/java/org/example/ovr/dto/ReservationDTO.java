package org.example.ovr.dto;

import org.example.ovr.Entity.Reservation;

import java.time.LocalDateTime;

public class ReservationDTO {

    private Long id;
    private String customerName;
    private String idType;
    private String idNumber;
    private String gender;
    private String country;
    private int roomNumber;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String phoneNumber;   // ✅ ADDED
    private String billPdfUrl;    // ✅ NEW: PDF URL for generated bill
    private String status;

    public ReservationDTO() {}

    // Constructor from Entity
    public ReservationDTO(Reservation r) {
        this.id = r.getId();
        this.customerName = r.getCustomerName();
        this.idType = r.getIdType();
        this.idNumber = r.getIdNumber();
        this.gender = r.getGender();
        this.country = r.getCountry();
        this.roomNumber = r.getRoom().getRoomNumber();
        this.checkIn = r.getCheckIn();
        this.checkOut = r.getCheckOut();
        this.phoneNumber = r.getPhoneNumber();
        this.status = r.getStatus();

    }

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getBillPdfUrl() {
        return billPdfUrl;
    }

    public void setBillPdfUrl(String billPdfUrl) {
        this.billPdfUrl = billPdfUrl;
    }


    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}