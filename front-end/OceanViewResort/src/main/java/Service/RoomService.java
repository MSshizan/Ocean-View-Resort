/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Api.ApiConfig;
import Api.ApiResponse;
import Api.HttpClientWithJwt;
import DTO.RoomDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;




/**
 * RoomService is a singleton class responsible for managing room-related operations
 * via HTTP requests to the backend.
 * 
 * It provides methods to add rooms, fetch all rooms, fetch a room by ID,
 * update room status and description, and filter rooms locally.
 * All network requests use HttpClientWithJwt to automatically attach JWT tokens.
 */
public class RoomService {

    // Singleton instance
    private static RoomService instance;

    // Base URL for room-related API endpoints
    private static final String BASE_URL = ApiConfig.BASE_URL + "/rooms";

    // For Testing Purpose
    public static void setInstance(RoomService service) {
    instance = service;
}
    
    
    // HTTP client with automatic JWT support
    private final HttpClientWithJwt client = new HttpClientWithJwt();

    // Private constructor for singleton pattern
     public RoomService() {}

    /**
     * Returns the singleton instance of RoomService.
     */
    public static RoomService getInstance() {
        if (instance == null) instance = new RoomService();
        return instance;
    }

    // ===== ADD ROOM =====
    /**
     * Adds a new room by sending a POST request to the backend.
     *
     * @param dto RoomDTO containing room details
     * @return ApiResponse containing the created RoomDTO or error info
     */
    public ApiResponse<RoomDTO> addRoom(RoomDTO dto) {
        try {
            return client.post(BASE_URL + "/add", dto, RoomDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<RoomDTO> err = new ApiResponse<>();
            err.setStatus(500);
            err.setMessage(e.getMessage());
            return err;
        }
    }

    // ===== GET ALL ROOMS =====
    /**
     * Fetches all rooms from the backend.
     * Converts array response from backend to a list for convenience.
     *
     * @return ApiResponse containing a list of RoomDTOs
     */
    public ApiResponse<List<RoomDTO>> getAllRooms() {
        try {
            ApiResponse<RoomDTO[]> response = client.get(BASE_URL + "/all", RoomDTO[].class);

            ApiResponse<List<RoomDTO>> result = new ApiResponse<>();
            result.setStatus(response.getStatus());
            result.setMessage(response.getMessage());
            result.setData(response.getData() != null ? Arrays.asList(response.getData()) : List.of());

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<List<RoomDTO>> err = new ApiResponse<>();
            err.setStatus(500);
            err.setMessage(e.getMessage());
            err.setData(List.of());
            return err;
        }
    }

    // ===== GET ROOM BY ID =====
    /**
     * Fetches a room by its ID.
     *
     * @param id Room ID
     * @return ApiResponse containing RoomDTO or error info
     */
    public ApiResponse<RoomDTO> getRoomById(int id) {
        try {
            return client.get(BASE_URL + "/" + id, RoomDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<RoomDTO> err = new ApiResponse<>();
            err.setStatus(500);
            err.setMessage(e.getMessage());
            return err;
        }
    }

    // ===== UPDATE ROOM STATUS & DESCRIPTION ONLY =====
    /**
     * Updates only the status and description of a room.
     * Sends a PUT request to the backend with updated RoomDTO.
     *
     * @param id Room ID
     * @param dto RoomDTO containing updated status and description
     * @return ApiResponse containing the updated RoomDTO or error info
     */
    public ApiResponse<RoomDTO> updateRoomStatusAndDescription(int id, RoomDTO dto) {
        try {
            return client.put(BASE_URL + "/update-status/" + id, dto, RoomDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<RoomDTO> err = new ApiResponse<>();
            err.setStatus(500);
            err.setMessage(e.getMessage());
            return err;
        }
    }

    // ===== FILTER ROOMS LOCALLY =====
    /**
     * Filters a list of rooms based on bed type and availability.
     *
     * @param rooms List of RoomDTO objects to filter
     * @param bedType Desired bed type (e.g., "Single", "Double")
     * @param onlyAvailable If true, include only rooms marked as "Available"
     * @return Filtered list of RoomDTOs
     */
    public List<RoomDTO> filterRooms(List<RoomDTO> rooms, String bedType, boolean onlyAvailable) {
        return rooms.stream()
                .filter(room -> room.getBedType().equalsIgnoreCase(bedType))
                .filter(room -> !onlyAvailable || "Available".equalsIgnoreCase(room.getAvailable()))
                .toList();
    }
}