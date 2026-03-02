package org.example.ovr.Entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    @Column(name = "room_number", nullable = false)
    private int roomNumber;

    @Column(name = "room_price", nullable = false)
    private double roomPrice;

    @Column(name = "number_of_days", nullable = false)
    private long numberOfDays;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "pdf_path")
    private String pdfPath;  // local storage path

    @Column(name = "pdf_url")
    private String pdfUrl;   // URL to send to frontend

    // ===============================
    // Constructors
    // ===============================

    public Bill() {
        this.createdDate = LocalDateTime.now();
    }

    public Bill(Long reservationId, int roomNumber, double roomPrice,
                long numberOfDays, double totalAmount, String pdfPath, String pdfUrl) {
        this.reservationId = reservationId;
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
        this.numberOfDays = numberOfDays;
        this.totalAmount = totalAmount;
        this.createdDate = LocalDateTime.now();
        this.pdfPath = pdfPath;
        this.pdfUrl = pdfUrl;
    }

    // ===============================
    // Getters and Setters
    // ===============================

    public Long getId() {
        return id;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        this.roomPrice = roomPrice;
    }

    public long getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(long numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
}