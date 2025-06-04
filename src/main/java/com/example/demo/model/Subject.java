package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"name", "standard"})
)
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g., "Mathematics"

    @Column(nullable = false)
    private String standard; // e.g., "10"

    @Column(nullable = false)
    private String section; // e.g., "A"


    public Subject() {}

    public Subject(String name, String standard, String section) {
        this.name = name;
        this.standard = standard;
        this.section = section;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStandard() { return standard; }
    public void setStandard(String standard) { this.standard = standard; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    
}
