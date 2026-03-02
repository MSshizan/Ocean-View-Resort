/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

public class RoomDTO {
    
    private Integer roomNumber;
    private String available;     // "Available" / "Not Available"
    private String checkStatus;   // "Clean" / "Dirty"
    private String bedType;       // e.g. "Single bed", "Double bed"
    private String description;
    private Double price;

    public RoomDTO() {}

    public RoomDTO(Integer roomNumber, String available, String checkStatus,
                   String bedType, String description, Double price) {
        this.roomNumber = roomNumber;
        this.available = available;
        this.checkStatus = checkStatus;
        this.bedType = bedType;
        this.description = description;
        this.price = price;
    }

    // Getters & Setters
    public Integer getRoomNumber() { return roomNumber; }
    public void setRoomNumber(Integer roomNumber) { this.roomNumber = roomNumber; }

    public String getAvailable() { return available; }
    public void setAvailable(String available) { this.available = available; }

    public String getCheckStatus() { return checkStatus; }
    public void setCheckStatus(String checkStatus) { this.checkStatus = checkStatus; }

    public String getBedType() { return bedType; }
    public void setBedType(String bedType) { this.bedType = bedType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

}
