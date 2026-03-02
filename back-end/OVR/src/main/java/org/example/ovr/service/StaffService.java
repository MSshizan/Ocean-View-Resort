package org.example.ovr.service;


import org.example.ovr.Entity.Staff;
import org.example.ovr.dto.StaffDTO;
import org.example.ovr.repository.StaffRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    public Staff addStaff(StaffDTO dto) {
        Staff staff = new Staff();
        staff.setName(dto.getName());
        staff.setAge(dto.getAge());
        staff.setGender(dto.getGender());
        staff.setDepartment(dto.getDepartment());
        staff.setJobRole(dto.getJobRole());
        staff.setContact(dto.getContact());
        staff.setEmail(dto.getEmail());
        return staffRepository.save(staff);
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    // you can add update, delete, etc.
}

