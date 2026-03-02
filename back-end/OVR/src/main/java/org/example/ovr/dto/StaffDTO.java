
package org.example.ovr.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.ovr.Entity.Staff;

public class StaffDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Age is required")
    @Positive(message = "Age must be positive")
    private Integer age;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Job Role is required")
    private String jobRole;

    @NotBlank(message = "Contact is required")
    private String contact;

    @Email(message = "Email must be valid")
    private String email;

    public StaffDTO() {}

    public StaffDTO(Staff staff) {
        this.name = staff.getName();
        this.age = staff.getAge();
        this.gender = staff.getGender();
        this.department = staff.getDepartment();
        this.jobRole = staff.getJobRole();
        this.contact = staff.getContact();
        this.email = staff.getEmail();
    }

    // ===== GETTERS & SETTERS =====
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
