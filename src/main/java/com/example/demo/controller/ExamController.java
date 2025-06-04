package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Exam;
import com.example.demo.model.Teacher;
import com.example.demo.service.ExamService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping("admin/students/exam/add")
    public String addExam(
            @RequestParam("name") String name,
            @RequestParam("standard") String standard,
            @RequestParam("section") String section,
            @RequestParam(value = "examDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate examDate,
            @RequestParam("examType") String examType,
            Model model) {
        System.out.println("Adding exam with details: " + name + ", " + standard + ", " + section + ", " + examDate
                + ", " + examType);
        try {
            Exam exam = new Exam();
            exam.setName(name);
            exam.setStandard(standard);
            exam.setSection(section);
            exam.setExamDate(examDate);
            exam.setExamType(examType);

            examService.saveExam(exam);

            return "success"; // Redirect to the form or another page
        } catch (Exception e) {
            model.addAttribute("error", "Error adding exam: " + e.getMessage());
            return "error"; // Redirect to an error page
        }
    }

    @GetMapping("/teachers/exams/get")
    public String getExamsByStandardSection(HttpSession session, Model model) {
        Teacher teacher = (Teacher) session.getAttribute("loggedInTeacher");
        if (teacher == null) {
            // Optionally, redirect to login or show error
            return "/";
        }

        String standard = teacher.getClassTeacherStandard();
        String section = teacher.getClassTeacherSection();

        System.out.println("Teacher's standard: " + standard);
        System.out.println("Teacher's section: " + section);

        if (standard == null || section == null) {
            // Optionally, handle case where teacher is not a class teacher
            model.addAttribute("error", "You are not assigned as a class teacher.");
            return "/"; // or a suitable error page
        }

        List<Exam> exams = examService.getExamsByStandardAndSection(standard, section);
        model.addAttribute("examsList", exams);
        System.out.println("Exams retrieved: " + exams.size());
    
        return "examlist"; // This should be the name of your Thymeleaf/JSP view
    }

}
