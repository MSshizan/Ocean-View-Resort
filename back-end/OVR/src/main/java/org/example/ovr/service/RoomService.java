package org.example.ovr.service;


import org.example.ovr.Entity.Room;
import org.example.ovr.dto.RoomDTO;
import org.example.ovr.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    // Add a new room
    public Room addRoom(RoomDTO dto) {
        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setAvailable(dto.getAvailable());
        room.setCheckStatus(dto.getCheckStatus());
        room.setBedType(dto.getBedType());
        room.setDescription(dto.getDescription());
        room.setPrice(dto.getPrice());
        return roomRepository.save(room);
    }


    // Get all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Get room by ID
    public Optional<Room> getRoomById(int id) {
        return roomRepository.findById(id);
    }

    // Update room
    public Room updateRoom(int id, RoomDTO dto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setRoomNumber(dto.getRoomNumber());
        room.setAvailable(dto.getAvailable());
        room.setCheckStatus(dto.getCheckStatus());
        room.setBedType(dto.getBedType());
        room.setDescription(dto.getDescription());
        room.setPrice(dto.getPrice());
        return roomRepository.save(room);
    }

    public Room updateRoomStatusAndDescription(int roomNumber, RoomDTO dto) {
        Room room = roomRepository.findById(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Update only status and description
        if (dto.getCheckStatus() != null && !dto.getCheckStatus().isEmpty()) {
            room.setCheckStatus(dto.getCheckStatus());
        }
        if (dto.getDescription() != null) {
            room.setDescription(dto.getDescription());
        }

        return roomRepository.save(room);
    }


    // Delete room
    public void deleteRoom(int id) {
        roomRepository.deleteById(id);
    }
}
