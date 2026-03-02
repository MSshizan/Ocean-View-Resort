package org.example.ovr.Entity;


import jakarta.persistence.*;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int age;
    private String gender;
    private String department;
    private String jobRole;
    private String contact;
    private String email;

    public Staff() {}

    public Staff(String name, int age, String gender, String department,
                 String jobRole, String contact, String email) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.department = department;
        this.jobRole = jobRole;
        this.contact = contact;
        this.email = email;
    }

    // ===== GETTERS & SETTERS =====
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

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
