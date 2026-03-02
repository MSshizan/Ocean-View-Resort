/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import Api.ApiConfig;
import Api.ApiResponse;
import Api.HttpClientWithJwt;
import DTO.StaffDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class StaffService {

    private static StaffService instance;
    private static final String BASE_URL = ApiConfig.BASE_URL + "/staff";
    private final HttpClientWithJwt client = new HttpClientWithJwt();

    private StaffService() {}

    public static StaffService getInstance() {
        if (instance == null) instance = new StaffService();
        return instance;
    }

    // No need for setJwtToken anymore

    public ApiResponse<StaffDTO> addStaff(StaffDTO dto) {
        try {
            return client.post(BASE_URL + "/add", dto, StaffDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<StaffDTO> err = new ApiResponse<>();
            err.setStatus(500);
            err.setMessage(e.getMessage());
            return err;
        }
    }
    
    
     // ================= GET ALL STAFF =================
    public ApiResponse<List<StaffDTO>> getAllStaff() {
        try {
            ApiResponse<StaffDTO[]> response =
                    client.get(BASE_URL + "/all", StaffDTO[].class);

            ApiResponse<List<StaffDTO>> result = new ApiResponse<>();
            result.setStatus(response.getStatus());
            result.setMessage(response.getMessage());
            result.setData(response.getData() != null
                    ? Arrays.asList(response.getData())
                    : List.of());

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<List<StaffDTO>> err = new ApiResponse<>();
            err.setStatus(500);
            err.setMessage(e.getMessage());
            err.setData(List.of());
            return err;
        }
    }

    // ================= FILTER BY DEPARTMENT =================
    public List<StaffDTO> filterByDepartment(List<StaffDTO> staffList,
                                             String department) {

        return staffList.stream()
                .filter(staff ->
                        staff.getDepartment() != null &&
                        staff.getDepartment().equalsIgnoreCase(department))
                .toList();
    }
}
