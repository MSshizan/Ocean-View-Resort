package org.example.ovr.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "id_type")
    private String idType;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "gender")
    private String gender;

    @Column(name = "country")
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_number", referencedColumnName = "room_number")
    private Room room;

    @Column(name = "check_in")
    private LocalDateTime checkIn;

    @Column(name = "check_out")
    private LocalDateTime checkOut;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String status =  "ACTIVE";

    public Reservation() { }

    // Constructor without ID (for creation)
    public Reservation(String customerName, String idType, String idNumber, String gender, String country,
                       Room room, LocalDateTime checkIn, LocalDateTime checkOut, String phoneNumber, String status) {
        this.customerName = customerName;
        this.idType = idType;
        this.idNumber = idNumber;
        this.gender = gender;
        this.country = country;
        this.room = room;
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

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    public LocalDateTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDateTime checkIn) { this.checkIn = checkIn; }

    public LocalDateTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDateTime checkOut) { this.checkOut = checkOut; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    // Helper to get room number easily
    public int getRoomNumber() {
        return room != null ? room.getRoomNumber() : 0;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}