package org.example.ovr.repository;

import org.example.ovr.Entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByReservationId(Long reservationId);
}