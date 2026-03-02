package org.example.ovr.service;

import jakarta.transaction.Transactional;
import org.example.ovr.Entity.Bill;
import org.example.ovr.Entity.Reservation;
import org.example.ovr.Entity.Room;
import org.example.ovr.dto.ReservationDTO;
import org.example.ovr.repository.ReservationRepository;
import org.example.ovr.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BillService billService;

    @Autowired
    private SmsService smsService; // Inject SMS service

    @Transactional
    public Reservation addReservation(ReservationDTO dto) {
        // Validate dates
        if (dto.getCheckIn() == null || dto.getCheckOut() == null) {
            throw new RuntimeException("Check-In and Check-Out dates are required");
        }
        if (!dto.getCheckOut().isAfter(dto.getCheckIn())) {
            throw new RuntimeException("Check-Out must be after Check-In");
        }
        if (dto.getCheckIn().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Check-In date cannot be in the past");
        }

        // Validate room
        Room room = roomRepository.findById(dto.getRoomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!"Available".equalsIgnoreCase(room.getAvailable().trim())) {
            throw new RuntimeException("Room is already booked!");
        }

        // Create reservation and set Room
        Reservation reservation = new Reservation();
        reservation.setCustomerName(dto.getCustomerName());
        reservation.setPhoneNumber(dto.getPhoneNumber());
        reservation.setIdType(dto.getIdType());
        reservation.setIdNumber(dto.getIdNumber());
        reservation.setGender(dto.getGender());
        reservation.setCountry(dto.getCountry());
        reservation.setCheckIn(dto.getCheckIn());
        reservation.setCheckOut(dto.getCheckOut());
        reservation.setRoom(room);
        reservationRepository.save(reservation);

        // 4️⃣ Update room status
        room.setAvailable("Booked");
        roomRepository.save(room);

        // 5️⃣ Generate Bill
        generateBillForReservation(reservation, room);

        // ✅ Send SMS and handle failure
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isBlank()) {
            String message = "Hi " + dto.getCustomerName() + ", your reservation (ID: "
                    + reservation.getId() + ") is confirmed from "
                    + dto.getCheckIn().toLocalDate() + " to "
                    + dto.getCheckOut().toLocalDate() + ". Thank you!";
            try {
                smsService.sendSms(dto.getPhoneNumber(), message);
            } catch (Exception e) {
                // Show message in console or throw to API layer
                System.err.println("⚠️ SMS could not be sent: " + e.getMessage());
                // Optional: throw to stop reservation creation if you want
                // throw new RuntimeException("Reservation created but SMS failed: " + e.getMessage());
            }
        }

        return reservation;
    }

    private void generateBillForReservation(Reservation reservation, Room room) {
        try {
            long numberOfDays = ChronoUnit.DAYS.between(reservation.getCheckIn().toLocalDate(),
                    reservation.getCheckOut().toLocalDate());
            if (numberOfDays == 0) numberOfDays = 1; // Minimum 1 day

            double roomPrice = room.getPrice();
            double totalAmount = roomPrice * numberOfDays;

            Bill bill = new Bill();
            bill.setReservationId(reservation.getId());
            bill.setRoomNumber(reservation.getRoomNumber());
            bill.setRoomPrice(roomPrice);
            bill.setNumberOfDays(numberOfDays);
            bill.setTotalAmount(totalAmount);
            bill.setCreatedDate(LocalDateTime.now());

            // Save bill
            Bill savedBill = billService.saveBill(bill);

            // Generate PDF
            billService.generateBillPdf(savedBill);

            // Update bill with PDF path
            billService.saveBill(savedBill);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public List<ReservationDTO> searchByDate(LocalDate date) {
        return reservationRepository
                .findByCheckInBetween(date.atStartOfDay(), date.atTime(23, 59, 59))
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        Room room = roomRepository.findById(reservation.getRoomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Free the room
        room.setAvailable("Available");
        roomRepository.save(room);

        // Delete bills
        billService.getBillsByReservationId(reservation.getId())
                .forEach(bill -> billService.deleteBill(bill.getId()));

        reservationRepository.delete(reservation);
    }

    private ReservationDTO convertToDTO(Reservation r) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(r.getId());
        dto.setCustomerName(r.getCustomerName());
        dto.setPhoneNumber(r.getPhoneNumber());
        dto.setIdType(r.getIdType());
        dto.setIdNumber(r.getIdNumber());
        dto.setGender(r.getGender());
        dto.setCountry(r.getCountry());
        dto.setRoomNumber(r.getRoomNumber());
        dto.setCheckIn(r.getCheckIn());
        dto.setCheckOut(r.getCheckOut());
        return dto;
    }

    @Transactional
    public void checkOutReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if ("CHECKED_OUT".equals(reservation.getStatus())) {
            throw new RuntimeException("Customer already checked out");
        }

        Room room = reservation.getRoom();

        room.setAvailable("Available");
        room.setCheckStatus("DIRTY");
        roomRepository.save(room);

        reservation.setStatus("CHECKED_OUT");
        reservationRepository.save(reservation);
    }
}