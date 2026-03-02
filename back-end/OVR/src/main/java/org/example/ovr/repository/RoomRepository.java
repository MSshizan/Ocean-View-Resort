package org.example.ovr.repository;

import org.example.ovr.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByBedType(String bedType);

    List<Room> findByBedTypeAndAvailableTrue(String bedType);
}