package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Exam;
import com.example.demo.model.Student;
import com.example.demo.model.Subject;
import com.example.demo.model.Teacher;
import com.example.demo.model.Mark;
import com.example.demo.service.ExamService;
import com.example.demo.service.MarkService;
import com.example.demo.service.StudentService;
import com.example.demo.service.SubjectService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MarkController {

    @Autowired
    private ExamService examService;

    @Autowired
    private MarkService markService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/teacher/exams/subjectmarks")
    public String addSubjectMarks(@RequestParam("examid") String examid, Model model, HttpSession session) {
        Long examId = Long.parseLong(examid);
        System.out.println("Exam ID received: " + examId);
        Teacher teacher = (Teacher) session.getAttribute("loggedInTeacher");
        if (teacher == null) {
            // Optionally, redirect to login or show error
            return "/";
        }

        String standard = teacher.getClassTeacherStandard();
        String section = teacher.getClassTeacherSection();

        List<Student> studentOfClassTeacher = studentService.getStudentsByClassAndSection(standard, section);
        model.addAttribute("studentOfClassTeacher", studentOfClassTeacher);

        Optional<Exam> exam = examService.getExamById(examId);
        if (exam == null) {
            model.addAttribute("error", "Exam not found");
            return "error"; // Redirect to an error page
        }
        model.addAttribute("selectedExam", exam.get());

        List<Subject> subjects = subjectService.getSubjectsByStandardAndSection(standard, section);
        model.addAttribute("subjectsList", subjects);
        return "marksentrypage";
    }

    @PostMapping("/teacher/exams/subjectmarks/save")
    public String saveStudentMarks(
            @RequestParam("rollno") Integer rollno,
            @RequestParam("examId") Long examId,
            @RequestParam Map<String, String> allParams,
            Model model, HttpSession session) {
        // 1. Fetch Exam
        Optional<Exam> exam = examService.getExamById(examId);
        Teacher teacher = (Teacher) session.getAttribute("loggedInTeacher");
        if (teacher == null) {
            // Optionally, redirect to login or show error
            return "/";
        }

        String standard = teacher.getClassTeacherStandard();
        String section = teacher.getClassTeacherSection();

        // 2. Fetch Student by roll number, standard, and section
        Student student = studentService.findByClassSectionAndRollno(
                standard, section, rollno);

        if (student == null) {
            model.addAttribute("successMessage", "Student with roll number " + rollno + " not found.");
            return "error";
        }

        // 3. Iterate over marks fields and save each Mark
        try {
            for (String key : allParams.keySet()) {
                if (key.startsWith("marks[")) {
                    String subjectIdStr = key.substring(6, key.length() - 1);
                    Long subjectId = Long.parseLong(subjectIdStr);

                    // Fetch all related fields for this subjectId
                    Double marks = allParams.get(key) != null ? Double.valueOf(allParams.get(key)) : null;
                    Double maxMarks = allParams.get("maxMarks[" + subjectId + "]") != null
                            ? Double.valueOf(allParams.get("maxMarks[" + subjectId + "]"))
                            : null;
                    String grade = allParams.getOrDefault("grade[" + subjectId + "]", "");
                    String remarks = allParams.getOrDefault("remarks[" + subjectId + "]", "");

                    Optional<Subject> subject = subjectService.getSubjectById(subjectId);

                    Mark mark = new Mark(student, exam.get(), subject.orElse(null), marks, maxMarks, grade, remarks);
                    // Save or update the mark (adjust your service method accordingly)
                    System.out.println("Saving mark: " + mark);
                    markService.saveMark(mark);
                }
            }
            return "success";
        } catch (Exception e) {
            model.addAttribute("error", "Error occurred while saving marks: " + e.getMessage());
            return "error";
        }
    }
}
