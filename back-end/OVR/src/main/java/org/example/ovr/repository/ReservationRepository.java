package org.example.ovr.repository;

import org.example.ovr.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // ✅ Check if room is booked during a date range
    boolean existsByRoom_RoomNumberAndCheckOutAfterAndCheckInBefore(
            int roomNumber, LocalDateTime checkIn, LocalDateTime checkOut
    );

    // ✅ Find reservations between two dates
    List<Reservation> findByCheckInBetween(LocalDateTime start, LocalDateTime end);
}