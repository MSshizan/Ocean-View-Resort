package org.example.ovr.Entity;


import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @Column(name = "room_number")
    private int roomNumber;

    @Column(name = "available")
    private String available;

    @Column(name = "check_status")
    private String checkStatus;

    @Column(name = "bed_type")
    private String bedType;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;


    public Room() {}

    public Room(int roomNumber, String available, String checkStatus,
                String bedType, String description, double price) {
        this.roomNumber = roomNumber;
        this.available = available;
        this.checkStatus = checkStatus;
        this.bedType = bedType;
        this.description = description;
        this.price = price;
    }


    public int getRoomNumber() { return roomNumber; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }

    public String getAvailable() { return available; }
    public void setAvailable(String available) { this.available = available; }

    public String getCheckStatus() { return checkStatus; }
    public void setCheckStatus(String checkStatus) { this.checkStatus = checkStatus; }

    public String getBedType() { return bedType; }
    public void setBedType(String bedType) { this.bedType = bedType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}