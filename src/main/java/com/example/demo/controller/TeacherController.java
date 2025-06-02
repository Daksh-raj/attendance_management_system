package com.example.demo.controller;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Teacher;
import com.example.demo.model.Student;
import com.example.demo.model.Attendance;

import com.example.demo.service.TeacherService;
import com.example.demo.service.StudentService;
import com.example.demo.service.AttendanceService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@Controller
public class TeacherController {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private AttendanceService attendanceService;


    @PostMapping("/admin/teachers/teachernewrecord/add")
    public String addTeachers(@RequestBody String teacherJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Teacher teacher;
        try {
            System.out.println(teacherJson);
            // Parse URL-encoded form data
            String[] pairs = teacherJson.split("&");
            Teacher s = new Teacher();
            for (String pair : pairs) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2) {
                    String key = java.net.URLDecoder.decode(kv[0], "UTF-8");
                    String value = java.net.URLDecoder.decode(kv[1], "UTF-8");
                    switch (key) {
                        case "name":
                            s.setName(value);
                            break;
                        case "email":
                            s.setEmail(value);
                            break;
                        case "password":
                            s.setPassword(value);
                            break;
                        case "classTeacherStandard":
                            s.setClassTeacherStandard(value);
                            break;
                        case "classTeacherSection":
                            s.setClassTeacherSection(value);
                            break;
                    }
                }
            }
            teacher = s;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid teacher data format", e);
        }
        if (teacher.getName() == null || teacher.getName().isEmpty() ||
                teacher.getEmail() == null || teacher.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Missing required teacher fields");
        }

        try {
            teacherService.saveTeacher(teacher);
            return "success"; // Redirect to the students page after saving
        } catch (Exception ex) {
            // Log the exception or handle it as needed
            return "error"; // Redirect to an error page
        }
    }

    @GetMapping("/admin/teachers/teachernewrecord")
    public String adminStudentManagement() {
        return "teachernewrecord"; // This will render src/main/resources/templates/students.html
    }

    @GetMapping("/teacher")
    public String teacher() {
        return "teacherslogin"; // This will render src/main/resources/templates/teacherslogin.html
    }

    @PostMapping("/teacher/login")
    public String loginTeacher(@RequestBody String loginRequest,Model model, HttpSession session) {

        ObjectMapper objectMapper = new ObjectMapper();
        String email="",password="";
        try{
            System.out.println(loginRequest);
            // Parse URL-encoded form data
            String[] pairs = loginRequest.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2) {
                    String key = java.net.URLDecoder.decode(kv[0], "UTF-8");
                    String value = java.net.URLDecoder.decode(kv[1], "UTF-8");
                    switch (key) {
                        case "email":
                            email=value;
                            break;
                        case "password":
                            password=value;
                            break;
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid request format", e);
        }

        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        Teacher teacher = null;
        // To print the teacher object as JSON instead of the default toString (which gives com.example.demo.model.Teacher@hashcode)
        
        teacher= teacherService.findByEmailAndPassword(email, password).orElse(null);
        System.out.println("Teacher: " + teacher);
        model.addAttribute("loggedInTeacher", teacher);
        session.setAttribute("loggedInTeacher", teacher);
        return "teachers";
    }
    @GetMapping("/teacher/login/viewstudents")
    public String viewStudents(Model model, HttpSession session) {
        System.out.println("Inside viewStudents method");
        Teacher loggedinTeacher = (Teacher) session.getAttribute("loggedInTeacher");
        String standard = loggedinTeacher.getClassTeacherStandard();
        String section = loggedinTeacher.getClassTeacherSection();
        System.out.println("Standard: " + standard);
        System.out.println("Section: " + section);
        try {
            if (standard == null || section == null) {
                model.addAttribute("error", "Missing class or section");
                return "teachers";
            }
            List<Student> studentOfClassTeacher = studentService.getStudentsByClassAndSection(standard, section);
            
            model.addAttribute("studentOfClassTeacher", studentOfClassTeacher);
            
            if (studentOfClassTeacher == null || studentOfClassTeacher.isEmpty()) {
                System.out.println("No students found for the selected class and section.");    
            } else {
                System.out.println("Found " + studentOfClassTeacher.size() + " students.");
                for (Student s : studentOfClassTeacher) {
                    System.out.println(s.getName() + " " + s.getRollno() + " " + s.getAge() + " " + s.getAddress() + " " + s.getPhone() + " " + s.getEmail()
                            + " " + s.getStandard() + " " + s.getSection());
                }
            }
            return "classteacherstudents"; // This will render src/main/resources/templates/students.html
        } catch (Exception e) {
            model.addAttribute("error", "Error occurred while fetching students");
            return "error";
        }
    }

    @GetMapping("/teacher/login/attendance/take")
        public String showAttendanceForm(
            HttpSession session,

            Model model
        ) {
            System.out.println("Inside showAttendanceForm method");
            Teacher loggedinTeacher = (Teacher) session.getAttribute("loggedInTeacher");
            String standard = loggedinTeacher.getClassTeacherStandard();
            String section = loggedinTeacher.getClassTeacherSection();
            List<Student> studentsList = studentService.getStudentsByClassAndSection(standard, section);
            // if (studentsList == null || studentsList.isEmpty()) {
            //     System.out.println("No students found for the selected class and section.");    
            // } else {
            //     System.out.println("Found " + studentsList.size() + " students.");
            //     for (Student s : studentsList) {
            //         System.out.println(s.getName() + " " + s.getRollno() + " " + s.getAge() + " " + s.getAddress() + " " + s.getPhone() + " " + s.getEmail()
            //                 + " " + s.getStandard() + " " + s.getSection());
            //     }
            // }
            model.addAttribute("studentsList", studentsList);
            model.addAttribute("date", LocalDate.now());
            return "attendancepage"; // Name of your HTML/Thymeleaf template
        }

        @PostMapping("/teacher/login/attendance/take")
        public String submitAttendance(
                @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                @RequestParam Map<String, String> allParams,
                HttpSession session,
                Model model) {

                Teacher loggedinTeacher = (Teacher) session.getAttribute("loggedInTeacher");
                String standard = loggedinTeacher.getClassTeacherStandard();
                String section = loggedinTeacher.getClassTeacherSection();
                List<Student> students = studentService.getStudentsByClassAndSection(standard, section);

            for (Student student : students) {
                String key = "attendance_" + student.getRollno();
                String status = allParams.get(key);
                System.out.println("Key: " + key + ", Status: " + status);
                boolean present = "present".equals(status);


                // Check if attendance already exists for this student and date
                Attendance optionalAttendance = null;
                optionalAttendance=attendanceService.getAttendanceByStudentAndDate(student, date);

                if (optionalAttendance != null) {
                    attendanceService.deleteAttendance(optionalAttendance);
                }
                Attendance attendance = new Attendance();
                attendance.setStudent(student);
                attendance.setDate(date);
                attendance.setPresent(present);
                attendanceService.saveAttendance(attendance);
            }

            model.addAttribute("message", "Attendance submitted successfully!");
            return "success"; // Or wherever you want to redirect
        }


}
