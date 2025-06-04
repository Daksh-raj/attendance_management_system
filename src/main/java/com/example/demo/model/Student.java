package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"standard", "section", "rollno"})
)
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer rollno;

    private String phone;

    private String email;

    
    private int age;
    private String address;
    @Column(nullable = false)   
    private String section;
    @Column(nullable = false)
    private String standard;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances = new ArrayList<>();

    public Student() {}

    public Student(String name, Integer rollno, int age, String address, String phone, String email, String standard, String section) {
        this.name = name;
        this.rollno = rollno;
        this.age = age;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.standard = standard;
        this.section = section;
    }

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getRollno() { return rollno; }
    public void setRollno(Integer rollno) { this.rollno = rollno; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public String getStandard() { return standard; }
    public void setStandard(String standard) { this.standard = standard; }

    public List<Attendance> getAttendances() { return attendances; }
    public void setAttendances(List<Attendance> attendances) { this.attendances = attendances; }

    public void addAttendance(Attendance attendance) {
        attendances.add(attendance);
        attendance.setStudent(this);
    }

    public void removeAttendance(Attendance attendance) {
        attendances.remove(attendance);
        attendance.setStudent(null);
    }
}
        