package org.example.ovr.service;

import org.example.ovr.Entity.Staff;
import org.example.ovr.dto.StaffDTO;
import org.example.ovr.repository.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class StaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private StaffService staffService;

    private StaffDTO staffDTO;
    private Staff staff;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        staffDTO = new StaffDTO();
        staffDTO.setName("Alice");
        staffDTO.setAge(30);
        staffDTO.setGender("Female");
        staffDTO.setDepartment("Front Desk");
        staffDTO.setJobRole("Receptionist");
        staffDTO.setContact("0771234567");
        staffDTO.setEmail("alice@example.com");

        staff = new Staff();
        staff.setName("Alice");
        staff.setAge(30);
        staff.setGender("Female");
        staff.setDepartment("Front Desk");
        staff.setJobRole("Receptionist");
        staff.setContact("0771234567");
        staff.setEmail("alice@example.com");
    }

    @Test
    void testAddStaff() {
        when(staffRepository.save(any(Staff.class))).thenReturn(staff);

        Staff savedStaff = staffService.addStaff(staffDTO);

        assertNotNull(savedStaff);
        assertEquals("Alice", savedStaff.getName());
        assertEquals(30, savedStaff.getAge());
        assertEquals("Front Desk", savedStaff.getDepartment());

        verify(staffRepository).save(any(Staff.class));
    }
}