package org.example.ovr.Controller;

import org.example.ovr.Entity.Bill;
import org.example.ovr.Entity.Reservation;
import org.example.ovr.Exception.ApiResponse;
import org.example.ovr.dto.ReservationDTO;
import org.example.ovr.service.BillService;
import org.example.ovr.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/reservations")
@CrossOrigin
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private BillService billService;


    @PostMapping("/add")
    public ApiResponse<ReservationDTO> addReservation(@RequestBody ReservationDTO dto) {
        try {
            // Add reservation
            Reservation reservation = reservationService.addReservation(dto);

            // Get the generated bill for this reservation
            List<Bill> bills = billService.getBillsByReservationId(reservation.getId());
            if (!bills.isEmpty()) {
                Bill bill = bills.get(0); // assuming one bill per reservation
                ReservationDTO resDto = new ReservationDTO(reservation);
                resDto.setBillPdfUrl(bill.getPdfUrl()); // add PDF URL to DTO
                return new ApiResponse<>(200, "Reservation and bill created successfully", resDto);
            }

            // Fallback if no bill
            return new ApiResponse<>(200, "Reservation created successfully, no bill found", new ReservationDTO(reservation));

        } catch (RuntimeException e) {
            return new ApiResponse<>(400, e.getMessage(), null);
        }
    }


    @GetMapping("/all")
    public ApiResponse<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> dtos = reservationService.getAllReservations()
                .stream()
                .map(reservation -> {
                    ReservationDTO dto = new ReservationDTO(reservation);
                    List<Bill> bills = billService.getBillsByReservationId(reservation.getId());
                    if (!bills.isEmpty()) {
                        dto.setBillPdfUrl(bills.get(0).getPdfUrl());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
        return new ApiResponse<>(200, "Success", dtos);
    }


    @PutMapping("/checkout/{id}")
    public ApiResponse<String> checkOut(@PathVariable Long id) {
        try {
            reservationService.checkOutReservation(id);
            return new ApiResponse<>(200, "Checked out successfully", "Success");
        } catch (RuntimeException e) {
            return new ApiResponse<>(400, e.getMessage(), null);
        }
    }

    // ===============================
    // ✅ Search reservations by date
    // ===============================
    @GetMapping("/search")
    public ApiResponse<List<ReservationDTO>> searchByDate(@RequestParam String date) {
        try {
            LocalDate searchDate = LocalDate.parse(date);
            List<ReservationDTO> list = reservationService.searchByDate(searchDate);
            return new ApiResponse<>(200, "Success", list);
        } catch (DateTimeParseException e) {
            return new ApiResponse<>(400, "Invalid date format. Use yyyy-MM-dd", null);
        }
    }

    // ===============================
    // ✅ Get reservation by ID
    // ===============================
    @GetMapping("/{id}")
    public ApiResponse<ReservationDTO> getReservationById(@PathVariable Long id) {
        Optional<Reservation> resOpt = reservationService.getReservationById(id);
        if (resOpt.isPresent()) {
            ReservationDTO dto = new ReservationDTO(resOpt.get());

            // Include bill PDF URL if exists
            List<Bill> bills = billService.getBillsByReservationId(id);
            if (!bills.isEmpty()) {
                dto.setBillPdfUrl(bills.get(0).getPdfUrl());
            }

            return new ApiResponse<>(200, "Success", dto);
        }
        return new ApiResponse<>(404, "Reservation not found", null);
    }

    // ===============================
    // ✅ Delete reservation (secured)
    // ===============================
    @PreAuthorize("hasRole('RECEPTIONIST') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservation(id);
            return new ApiResponse<>(200, "Reservation deleted successfully", "Success");
        } catch (RuntimeException e) {
            return new ApiResponse<>(400, e.getMessage(), null);
        }
    }
}