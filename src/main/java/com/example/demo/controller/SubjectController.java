package com.example.demo.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Subject;
import com.example.demo.service.SubjectService;

@Controller
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping("admin/students/subject/add")
    public String addExam(
            @RequestParam("name") String name,
            @RequestParam("standard") String standard,
            @RequestParam("section") String section,
            Model model
    ) {
        System.out.println("Adding exam with details: " + name + ", " + standard + ", " + section );
        try{
            Subject subject = new Subject();
        subject.setName(name);
        subject.setStandard(standard);
        subject.setSection(section);

        subjectService.saveSubject(subject);

        return "success"; // Redirect to the form or another page
        }
        catch (Exception e) {
            model.addAttribute("error", "Error adding exam: " + e.getMessage());
            return "error"; // Redirect to an error page
        }
    }
}
