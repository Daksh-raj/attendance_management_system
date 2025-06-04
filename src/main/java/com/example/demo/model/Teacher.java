package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"classTeacherStandard", "classTeacherSection"})
)
@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String subject;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String password;

    
    private String classTeacherStandard;
    private String classTeacherSection;

    public Teacher() {
    }

    public Teacher(Long id, String name, String subject, String email, String password) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

   public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getClassTeacherStandard() { return classTeacherStandard; }
    public void setClassTeacherStandard(String classTeacherStandard) { this.classTeacherStandard = classTeacherStandard; }

    public void setClassTeacherSection(String classTeacherSection) { this.classTeacherSection = classTeacherSection; }
    public String getClassTeacherSection() { return classTeacherSection; }
    
    public void setSubject(String subject) { this.subject = subject; }
    public String getSubject() { return subject; }

    
}