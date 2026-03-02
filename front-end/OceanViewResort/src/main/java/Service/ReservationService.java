package Service;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import Api.ApiConfig;
import DTO.ReservationDTO;
import Api.ApiResponse;
import Api.HttpClientWithJwt;
import java.util.Arrays;
import java.util.List;


/**
 * ReservationService is a singleton class that manages client-side communication
 * with the backend reservation API.
 * It provides methods for fetching, searching, adding, checking out, and deleting reservations.
 * All requests use HttpClientWithJwt, which automatically attaches JWT tokens for authorization.
 */
public class ReservationService {

    // Singleton instance
    private static ReservationService instance;

    // Base URL for reservation-related API endpoints
    private static final String BASE_URL = ApiConfig.BASE_URL + "/reservations";

    // HttpClientWithJwt instance for making authenticated HTTP requests
    private final HttpClientWithJwt client = new HttpClientWithJwt();

    // Private constructor for singleton pattern
    private ReservationService() {}

    /**
     * Returns the singleton instance of ReservationService.
     */
    public static ReservationService getInstance() {
        if (instance == null) instance = new ReservationService();
        return instance;
    }

    // ===== GET ALL RESERVATIONS =====
    /**
     * Fetches all reservations from the backend.
     *
     * @return ApiResponse containing a list of ReservationDTO objects
     */
    public ApiResponse<List<ReservationDTO>> getAllReservations() {
        try {
            // Backend may return an array of reservations
            ApiResponse<ReservationDTO[]> response =
                    client.get(BASE_URL + "/all", ReservationDTO[].class);

            // Convert array to list and wrap in ApiResponse
            ApiResponse<List<ReservationDTO>> result = new ApiResponse<>();
            result.setStatus(response.getStatus());
            result.setMessage(response.getMessage());
            result.setData(response.getData() != null ? Arrays.asList(response.getData()) : List.of());

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<List<ReservationDTO>> err = new ApiResponse<>();
            err.setStatus(500);
            err.setMessage(e.getMessage());
            err.setData(List.of());
            return err;
        }
    }

    // ===== SEARCH RESERVATIONS BY DATE =====
    /**
     * Searches reservations by a specific date.
     *
     * @param date Reservation date as a String (format depends on backend)
     * @return ApiResponse containing a list of matching ReservationDTOs
     */
    public ApiResponse<List<ReservationDTO>> searchByDate(String date) {
        try {
            ApiResponse<ReservationDTO[]> response = client.get(
                    BASE_URL + "/search?date=" + date, ReservationDTO[].class);

            ApiResponse<List<ReservationDTO>> result = new ApiResponse<>();
            result.setStatus(response.getStatus());
            result.setMessage(response.getMessage());
            result.setData(response.getData() != null ? Arrays.asList(response.getData()) : List.of());

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<List<ReservationDTO>> err = new ApiResponse<>();
            err.setStatus(500);
            err.setMessage(e.getMessage());
            err.setData(List.of());
            return err;
        }
    }

    // ===== ADD CUSTOMER RESERVATION =====
    /**
     * Adds a new reservation for a customer.
     *
     * @param dto ReservationDTO containing reservation details
     * @return ApiResponse with the created ReservationDTO
     */
    public ApiResponse<ReservationDTO> addCustomer(ReservationDTO dto) {
        try {
            return client.post(BASE_URL + "/add", dto, ReservationDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<ReservationDTO> err = new ApiResponse<>();
            err.setStatus(500);
            err.setMessage(e.getMessage());
            return err;
        }
    }

    // ===== CHECK OUT =====
    /**
     * Checks out a reservation by updating its status in the backend.
     *
     * @param id Reservation ID
     * @return ApiResponse containing success/error message
     */
    public ApiResponse<String> checkOut(Long id) {
        try {
            return client.put(BASE_URL + "/checkout/" + id, null, String.class);
        } catch (Exception e) {
            ApiResponse<String> err = new ApiResponse<>();
            err.setStatus(500);
            err.setMessage(e.getMessage());
            return err;
        }
    }

    // ===== GET RESERVATION BY ID =====
    /**
     * Fetches a reservation by its ID.
     *
     * @param id Reservation ID
     * @return ApiResponse containing the ReservationDTO or error info
     */
    public ApiResponse<ReservationDTO> getReservationById(Long id) {
        try {
            return client.get(BASE_URL + "/" + id, ReservationDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<ReservationDTO> err = new ApiResponse<>();
            err.setStatus(500);
            err.setMessage(e.getMessage());
            err.setData(null);
            return err;
        }
    }

    // ===== DELETE RESERVATION =====
    /**
     * Deletes a reservation by its ID.
     *
     * @param id Reservation ID
     * @return ApiResponse containing success/error message
     */
    public ApiResponse<String> deleteReservation(Long id) {
        try {
            return client.delete(BASE_URL + "/" + id, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<String> err = new ApiResponse<>();
            err.setStatus(500);
            err.setMessage(e.getMessage());
            return err;
        }
    }
}