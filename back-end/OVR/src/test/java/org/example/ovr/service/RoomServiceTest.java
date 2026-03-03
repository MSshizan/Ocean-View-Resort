package org.example.ovr.service;


import org.example.ovr.Entity.Room;
import org.example.ovr.dto.RoomDTO;
import org.example.ovr.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    private Room room;
    private RoomDTO roomDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        roomDTO = new RoomDTO();
        roomDTO.setRoomNumber(101);
        roomDTO.setAvailable("Available");
        roomDTO.setCheckStatus("Clean");
        roomDTO.setBedType("Double");
        roomDTO.setDescription("Sea view");
        roomDTO.setPrice(120.0);

        room = new Room();
        room.setRoomNumber(101);
        room.setAvailable("Available");
        room.setCheckStatus("Clean");
        room.setBedType("Double");
        room.setDescription("Sea view");
        room.setPrice(120.0);
    }

    @Test
    void testAddRoom() {
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room savedRoom = roomService.addRoom(roomDTO);

        assertNotNull(savedRoom);
        assertEquals(101, savedRoom.getRoomNumber());
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void testGetAllRooms() {
        when(roomRepository.findAll()).thenReturn(Arrays.asList(room));

        List<Room> rooms = roomService.getAllRooms();

        assertNotNull(rooms);
        assertEquals(1, rooms.size());
        verify(roomRepository).findAll();
    }

    @Test
    void testGetRoomById() {
        when(roomRepository.findById(101)).thenReturn(Optional.of(room));

        Optional<Room> result = roomService.getRoomById(101);

        assertTrue(result.isPresent());
        assertEquals(101, result.get().getRoomNumber());
        verify(roomRepository).findById(101);
    }

    @Test
    void testUpdateRoom() {
        when(roomRepository.findById(101)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room updatedRoom = roomService.updateRoom(101, roomDTO);

        assertNotNull(updatedRoom);
        assertEquals("Available", updatedRoom.getAvailable());
        verify(roomRepository).findById(101);
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void testUpdateRoomStatusAndDescription() {
        when(roomRepository.findById(101)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        RoomDTO updateDTO = new RoomDTO();
        updateDTO.setCheckStatus("Dirty");
        updateDTO.setDescription("Garden view");

        Room updatedRoom = roomService.updateRoomStatusAndDescription(101, updateDTO);

        assertEquals("Dirty", updatedRoom.getCheckStatus());
        assertEquals("Garden view", updatedRoom.getDescription());
        verify(roomRepository).findById(101);
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void testDeleteRoom() {
        doNothing().when(roomRepository).deleteById(101);

        roomService.deleteRoom(101);

        verify(roomRepository).deleteById(101);
    }
}
