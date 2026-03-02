package org.example.ovr.Controller;


import org.example.ovr.Entity.Bill;
import org.example.ovr.Exception.ApiResponse;
import org.example.ovr.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// Marks this class as a REST controller so Spring can handle HTTP requests and return JSON responses
@RestController

// Base URL mapping for all endpoints in this controller
@RequestMapping("/api/bills")

// Allows Cross-Origin requests (useful for front-end apps on a different domain)
@CrossOrigin
public class BillController {

    // Injects BillService to handle business logic related to bills
    @Autowired
    private BillService billService;

    // ===== CREATE BILL =====
    // Handles POST requests to create a new bill
    @PostMapping("/add")
    public ApiResponse<Bill> createBill(@RequestBody Bill bill) {  // Maps incoming JSON to Bill object
        try {
            // Auto-calculate total amount if number of days and room price are set
            if (bill.getNumberOfDays() > 0 && bill.getRoomPrice() > 0) {
                bill.setTotalAmount(bill.getRoomPrice() * bill.getNumberOfDays());
            }

            // Set the creation date to the current date and time
            bill.setCreatedDate(java.time.LocalDateTime.now());

            // Save the bill in the database first to generate an ID
            Bill savedBill = billService.saveBill(bill);

            // Generate a PDF of the bill and update the bill with PDF info (path & URL)
            Bill billWithPdf = billService.generateBillPdf(savedBill);

            // Return success response with the saved bill
            return new ApiResponse<>(200, "Bill created successfully", billWithPdf);
        } catch (Exception e) {
            e.printStackTrace();
            // Return error response in case of exceptions
            return new ApiResponse<>(400, "Error creating bill: " + e.getMessage(), null);
        }
    }

    // ===== GET ALL BILLS =====
    // Handles GET requests to fetch all bills
    @GetMapping("/all")
    public ApiResponse<List<Bill>> getAllBills() {
        try {
            // Retrieve all bills from the database
            List<Bill> bills = billService.getAllBills();
            return new ApiResponse<>(200, "Success", bills);
        } catch (Exception e) {
            e.printStackTrace();
            // Return error response if something goes wrong
            return new ApiResponse<>(400, "Error fetching bills: " + e.getMessage(), null);
        }
    }

    // ===== GET BILLS BY RESERVATION =====
    // Handles GET requests to fetch bills related to a specific reservation
    @GetMapping("/reservation/{reservationId}")
    public ApiResponse<List<Bill>> getBillsByReservation(@PathVariable Long reservationId) {
        try {
            // Retrieve bills for the given reservation ID
            List<Bill> bills = billService.getBillsByReservationId(reservationId);

            // If no bills found, return 404 response
            if (bills.isEmpty()) {
                return new ApiResponse<>(404, "No bills found for this reservation", null);
            }

            return new ApiResponse<>(200, "Success", bills);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(400, "Error fetching bills: " + e.getMessage(), null);
        }
    }

    // ===== DELETE BILL =====
    // Handles DELETE requests to remove a bill by ID
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteBill(@PathVariable Long id) {
        try {
            // Delete bill using service
            billService.deleteBill(id);
            return new ApiResponse<>(200, "Bill deleted successfully", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            // Return error response if deletion fails
            return new ApiResponse<>(400, "Error deleting bill: " + e.getMessage(), null);
        }
    }
}