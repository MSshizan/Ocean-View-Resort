package org.example.ovr.Controller;

import org.example.ovr.Entity.Room;
import org.example.ovr.Exception.ApiResponse;
import org.example.ovr.dto.RoomDTO;
import org.example.ovr.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;




// Marks this class as a REST controller so Spring can handle HTTP requests and return JSON responses
@RestController

// Base URL mapping for all endpoints in this controller
@RequestMapping("/api/rooms")
public class RoomController {

    // Injects RoomService to handle business logic related to rooms
    @Autowired
    private RoomService roomService;

    // ===== ADD ROOM =====
    // Only users with ADMIN role can access this endpoint
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ApiResponse<RoomDTO> addRoom(@RequestBody RoomDTO dto) {  // Maps incoming JSON to RoomDTO

        // Get the currently authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("=== RoomController.addRoom ===");
        System.out.println("Current principal: " + auth.getPrincipal());
        System.out.println("Authorities: " + auth.getAuthorities());

        // Call service to add a new room
        Room room = roomService.addRoom(dto);

        // Return success response with newly added room info
        return new ApiResponse<>(200, "Room added successfully", new RoomDTO(room));
    }

    // ===== GET ALL ROOMS =====
    // Public endpoint to fetch all rooms
    @GetMapping("/all")
    public ApiResponse<List<RoomDTO>> getAllRooms() {
        // Get the currently authenticated user (for logging/debugging)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("=== RoomController.getAllRooms ===");
        System.out.println("Current principal: " + auth.getPrincipal());
        System.out.println("Authorities: " + auth.getAuthorities());

        // Convert Room entities to RoomDTOs for the response
        List<RoomDTO> dtos = roomService.getAllRooms()
                .stream()
                .map(RoomDTO::new)
                .collect(Collectors.toList());

        return new ApiResponse<>(200, "Success", dtos);
    }

    // ===== UPDATE ROOM STATUS & DESCRIPTION =====
    // Updates only the status and description of a room
    @PutMapping("/update-status/{id}")
    public ApiResponse<RoomDTO> updateRoomStatusAndDescription(
            @PathVariable int id,      // Room ID from URL
            @RequestBody RoomDTO dto   // New status & description
    ) {
        Room updated = roomService.updateRoomStatusAndDescription(id, dto);
        return new ApiResponse<>(200, "Room status & description updated successfully", new RoomDTO(updated));
    }

    // ===== GET ROOM BY ID =====
    // Fetch a single room using its ID
    @GetMapping("/{id}")
    public ApiResponse<RoomDTO> getRoomById(@PathVariable int id) {
        Optional<Room> roomOpt = roomService.getRoomById(id);

        // If room exists, return it; otherwise, return 404
        return roomOpt
                .map(room -> new ApiResponse<>(200, "Success", new RoomDTO(room)))
                .orElseGet(() -> new ApiResponse<>(404, "Room not found", null));
    }

    // ===== UPDATE ROOM =====
    // Only ADMIN can update all room details
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<RoomDTO> updateRoom(@PathVariable int id, @RequestBody RoomDTO dto) {
        Room updated = roomService.updateRoom(id, dto);
        return new ApiResponse<>(200, "Room updated successfully", new RoomDTO(updated));
    }

    // ===== DELETE ROOM =====
    // Only ADMIN can delete a room
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteRoom(@PathVariable int id) {
        roomService.deleteRoom(id);
        return new ApiResponse<>(200, "Room deleted successfully", "Success");
    }
}