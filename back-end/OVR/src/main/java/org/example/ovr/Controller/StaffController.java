package org.example.ovr.Controller;



import org.example.ovr.Entity.Staff;
import org.example.ovr.Exception.ApiResponse;
import org.example.ovr.dto.StaffDTO;
import org.example.ovr.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

// Marks this class as a REST controller so Spring can handle HTTP requests and return JSON responses
@RestController

// Base URL mapping for all endpoints in this controller
@RequestMapping("/api/staff")
public class StaffController {

    // Injects StaffService to handle business logic related to staff
    @Autowired
    private StaffService staffService;

    // ===== ADD STAFF =====
    // Handles POST requests to add a new staff member
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<StaffDTO>> addStaff(
            @Valid @RequestBody StaffDTO dto) {  // Maps incoming JSON to StaffDTO and validates it

        // Call service to add new staff
        Staff staff = staffService.addStaff(dto);

        // Convert the saved Staff entity to DTO for response
        StaffDTO responseDto = new StaffDTO(staff);

        // Return success response with added staff info
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Staff added successfully", responseDto)
        );
    }

    // ===== GET ALL STAFF =====
    // Handles GET requests to fetch all staff members
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<StaffDTO>>> getAllStaff() {

        // Convert Staff entities to StaffDTOs for the response
        List<StaffDTO> dtos = staffService.getAllStaff()
                .stream()
                .map(StaffDTO::new)
                .collect(Collectors.toList());

        // Return success response with list of all staff
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Success", dtos)
        );
    }
}