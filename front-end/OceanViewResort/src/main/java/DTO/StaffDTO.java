/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

public class StaffDTO {

    private String name;
    private Integer age;
    private String gender;
    private String department;
    private String jobRole;
    private String contact;
    private String email;

    // ===== Constructors =====
    public StaffDTO() {}

    public StaffDTO(String name, Integer age, String gender,
                    String department, String jobRole,
                    String contact, String email) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.department = department;
        this.jobRole = jobRole;
        this.contact = contact;
        this.email = email;
    }

    // ===== Getters & Setters =====
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

    // Optional: Create DTO from Staff entity
   
}

