package org.example.ovr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.ovr.Entity.Room;

public class RoomDTO {

    @NotNull(message = "Room number is required")
    @Positive(message = "Room number must be positive")
    private Integer roomNumber;

    @NotBlank(message = "Availability is required")
    private String available;

    @NotBlank(message = "Check status is required")
    private String checkStatus; // e.g., Clean/Dirty

    @NotBlank(message = "Bed type is required")
    private String bedType;

    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;


    public RoomDTO() {
    }

    public RoomDTO(Room room) {
        this.roomNumber = room.getRoomNumber();
        this.available = room.getAvailable();
        this.checkStatus = room.getCheckStatus();
        this.bedType = room.getBedType();
        this.description = room.getDescription();
        this.price = room.getPrice();

    }

    // ===== GETTERS & SETTERS =====
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
