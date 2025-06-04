package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"name", "standard", "section"})
)
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g., "PT1", "SA1"

    @Column(nullable = false)
    private String standard;

    @Column(nullable = false)
    private String section;

    private LocalDate examDate;

    @Column(nullable = false)
    private String examType; // e.g., "Periodic", "Summative"

    public Exam() {}

    public Exam(String name, String standard, String section, LocalDate examDate, String examType) {
        this.name = name;
        this.standard = standard;
        this.section = section;
        this.examDate = examDate;
        this.examType = examType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStandard() { return standard; }
    public void setStandard(String standard) { this.standard = standard; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }

    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }
}
