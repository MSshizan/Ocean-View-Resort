package org.example.ovr.service;


import org.example.ovr.Entity.Bill;
import org.example.ovr.Entity.Reservation;
import org.example.ovr.Entity.Room;
import org.example.ovr.dto.ReservationDTO;
import org.example.ovr.repository.ReservationRepository;
import org.example.ovr.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BillService billService;

    @Mock
    private SmsService smsService;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void testAddReservationSuccess() {
        ReservationDTO dto = new ReservationDTO();
        dto.setCustomerName("John Doe");
        dto.setPhoneNumber("0771234567");
        dto.setCheckIn(LocalDateTime.now().plusDays(1));
        dto.setCheckOut(LocalDateTime.now().plusDays(3));
        dto.setRoomNumber(201);

        Room room = new Room();
        room.setRoomNumber(201);
        room.setAvailable("Available");
        room.setPrice(100.0);

        Reservation savedReservation = new Reservation();
        savedReservation.setId(12L);
        savedReservation.setRoom(room);

        when(roomRepository.findById(201)).thenReturn(Optional.of(room));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);
        when(billService.saveBill(any())).thenReturn(new Bill());

        Reservation result = reservationService.addReservation(dto);

        assertNotNull(result);
        assertEquals(savedReservation.getId(), result.getId());
        assertEquals("Booked", room.getAvailable());

        verify(reservationRepository).save(any(Reservation.class));
        verify(roomRepository).save(room);
        verify(billService).saveBill(any());
        verify(billService).generateBillPdf(any());
        verify(smsService).sendSms(eq("0771234567"), anyString());
    }


    @Test
    void testAddReservationRoomAlreadyBooked() {
        ReservationDTO dto = new ReservationDTO();
        dto.setCustomerName("Alice");
        dto.setRoomNumber(101);
        dto.setCheckIn(LocalDateTime.now().plusDays(1));
        dto.setCheckOut(LocalDateTime.now().plusDays(2));

        Room room = new Room();
        room.setRoomNumber(101);
        room.setAvailable("Booked"); // Already booked

        when(roomRepository.findById(101)).thenReturn(Optional.of(room));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservationService.addReservation(dto);
        });

        assertEquals("Room is already booked!", exception.getMessage());
        verify(reservationRepository, never()).save(any());
        verify(roomRepository, never()).save(any());
        verify(billService, never()).saveBill(any());
        verify(smsService, never()).sendSms(anyString(), anyString());
    }

    @Test
    void testAddReservationCheckInInPast() {
        ReservationDTO dto = new ReservationDTO();
        dto.setCustomerName("Bob");
        dto.setRoomNumber(102);
        dto.setCheckIn(LocalDateTime.now().minusDays(1)); // Past date
        dto.setCheckOut(LocalDateTime.now().plusDays(2));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservationService.addReservation(dto);
        });

        assertEquals("Check-In date cannot be in the past", exception.getMessage());
        verify(reservationRepository, never()).save(any());
        verify(roomRepository, never()).save(any());
        verify(billService, never()).saveBill(any());
        verify(smsService, never()).sendSms(anyString(), anyString());
    }

    @Test
    void testAddReservationCheckOutBeforeCheckIn() {
        ReservationDTO dto = new ReservationDTO();
        dto.setCustomerName("Charlie");
        dto.setRoomNumber(103);
        dto.setCheckIn(LocalDateTime.now().plusDays(3));
        dto.setCheckOut(LocalDateTime.now().plusDays(2)); // Check-out before check-in

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservationService.addReservation(dto);
        });

        assertEquals("Check-Out must be after Check-In", exception.getMessage());
        verify(reservationRepository, never()).save(any());
        verify(roomRepository, never()).save(any());
        verify(billService, never()).saveBill(any());
        verify(smsService, never()).sendSms(anyString(), anyString());
    }


}