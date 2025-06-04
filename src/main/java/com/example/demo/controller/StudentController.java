package com.example.demo.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Attendance;
import com.example.demo.model.Exam;
import com.example.demo.model.Mark;
import com.example.demo.model.Student;
import com.example.demo.model.Subject;
import com.example.demo.service.AttendanceService;
import com.example.demo.service.ExamService;
import com.example.demo.service.MarkService;
import com.example.demo.service.StudentService;
import com.example.demo.service.SubjectService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private ExamService examService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private MarkService markService;

    @GetMapping("/")
    public String homePage() {
        try {
            return "home";
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/studentspage/getSingleStudentInfo")
    public String findStudentByClassSectionAndRollno(
            @RequestBody String formData,
            Model model) {
        try {
            // Parse URL-encoded form data
            String[] pairs = formData.split("&");
            String standard = null, section = null;
            Integer rollno = null;
            for (String pair : pairs) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2) {
                    String key = java.net.URLDecoder.decode(kv[0], "UTF-8");
                    String value = java.net.URLDecoder.decode(kv[1], "UTF-8");
                    switch (key) {
                        case "standard":
                            standard = value;
                            break;
                        case "section":
                            section = value;
                            break;
                        case "rollno":
                            rollno = Integer.parseInt(value);
                            break;
                    }
                }
            }

            if (standard == null || section == null || rollno == null) {
                System.out.println("Missing required fields: standard, section, or rollno");
                model.addAttribute("error", "Missing required fields");
                return "error";
            }
            Student singleStudent = null;
            singleStudent = studentService.findByClassSectionAndRollno(standard, section,

                    rollno);
            model.addAttribute("singleStudent", singleStudent);
            System.out.println("single Student: " + singleStudent);
        } catch (Exception e) {
            model.addAttribute("error", "Error occurred while fetching student");
            return "error";
        }
        return "studentshomepage";
    }

    @GetMapping("/studentspage/getSingleStudentInfo/attendance/chart")
    public String showAttendanceChart(
            @RequestParam("studentId") String studentId,
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            Model model) {
        Long studentIdLong;
        try {
            studentIdLong = Long.parseLong(studentId);
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Invalid student ID format");
            return "error";
        }
        System.out.println("Student ID: " + studentIdLong);
        System.out.println("Year: " + year);
        System.out.println("Month: " + month);

        Optional<Student> studentChart = studentService.getStudentById(studentIdLong);
        List<Attendance> attendances = attendanceService.getAttendanceForMonth(studentChart, year, month);

        // Prepare a map of date -> present/absent for the month
        Map<LocalDate, Boolean> attendanceMap = new HashMap<>();
        attendances.forEach(a -> attendanceMap.put(a.getDate(), a.isPresent()));
        long presentCount = attendanceMap.values().stream().filter(Boolean::booleanValue).count();
        long absentCount = attendanceMap.size() - presentCount;
        model.addAttribute("presentCount", presentCount);
        model.addAttribute("absentCount", absentCount);
        System.out.println("Attendance Map: " + attendanceMap);
        model.addAttribute("studentChart", studentChart.get());
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("attendanceMap", attendanceMap);

        return "attendancechart";
    }

    // Handles GET request with standard and section as query parameters
    @GetMapping("/admin/students/studentviewrecord/show")
    public String showStudentsByClassAndSectionQuery(
            @RequestParam("standard") String standard,
            @RequestParam("section") String section,
            Model model) {
        try {
            if (standard == null || section == null) {
                model.addAttribute("error", "Missing class or section");
                return "studentviewrecord";
            }
            List<Student> studentByClassAndSection = studentService.getStudentsByClassAndSection(standard, section);

            model.addAttribute("studentByClassAndSection", studentByClassAndSection);

            // if (studentByClassAndSection == null || studentByClassAndSection.isEmpty()) {
            // System.out.println("No students found for the selected class and section.");
            // } else {
            // System.out.println("Found " + studentByClassAndSection.size() + "
            // students.");
            // for (Student s : studentByClassAndSection) {
            // System.out.println(s.getName() + " " + s.getRollno() + " " + s.getAge() + " "
            // + s.getAddress() + " " + s.getPhone() + " " + s.getEmail()
            // + " " + s.getStandard() + " " + s.getSection());
            // }
            // }
            return "studentviewrecordshow";
        } catch (Exception e) {
            model.addAttribute("error", "Error occurred while fetching students");
            return "error";
        }
    }

    @GetMapping("/studentspage")
    public String showStudentsPage() {
        return "studentspage"; // This will render src/main/resources/templates/students.html
    }

    // @GetMapping("/student")
    // public String showStudentsPage() {
    // return "studentspage"; // This will render
    // src/main/resources/templates/students.html
    // }

    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin"; // This will render src/main/resources/templates/students.html
    }

    @GetMapping("/admin/teachers")
    public String showAdminTeachersManagementPage() {
        return "adminteachers"; // This will render src/main/resources/templates/students.html
    }

    @GetMapping("/admin/students")
    public String showAdminStudentsManagementPage() {
        return "adminstudentmanage"; // This will render src/main/resources/templates/students.html
    }

    @GetMapping("/admin/students/crud")
    public String showAdminStudentsCrudPage() {
        return "adminstudents"; // This will render src/main/resources/templates/students.html
    }

    @GetMapping("/admin/students/examsubjectmanage")
    public String showAdminStudentsExamSubjectPage() {
        return "adminexamsubjectpage"; // This will render src/main/resources/templates/students.html
    }

    @GetMapping("/admin/students/exam")
    public String adminStudentExamPage() {
        return "adminexampage"; // This will render src/main/resources/templates/students.html
    }

    @GetMapping("/admin/students/subject")
    public String adminStudentSubjectPage() {
        return "adminsubjectpage"; // This will render src/main/resources/templates/students.html
    }

    @GetMapping("/admin/students/studentnewrecord")
    public String adminStudentManagement() {
        return "studentnewrecord"; // This will render src/main/resources/templates/students.html
    }

    @GetMapping("/admin/students/studentviewrecord")
    public String adminViewRecord() {
        return "studentviewrecord"; // This will render src/main/resources/templates/studentviewrecord.html
    }

    @GetMapping("/admin/students/deletestudentrecord")
    public String adminDeleteRecord() {
        return "studentdeleterecord"; // This will render src/main/resources/templates/deleterecord.html
    }

    @PostMapping("/admin/students/deletestudentrecord")
    public String addStudents(@RequestBody String studentJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        String standard = "";
        String section = "";
        Integer rollno = null;
        try {
            // Parse URL-encoded form data
            String[] pairs = studentJson.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2) {
                    String key = java.net.URLDecoder.decode(kv[0], "UTF-8");
                    String value = java.net.URLDecoder.decode(kv[1], "UTF-8");
                    switch (key) {
                        case "rollno":
                            rollno = Integer.parseInt(value);
                            break;
                        case "standard":
                            standard = value;
                            break;
                        case "section":
                            section = value;
                            break;
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid student data format", e);
        }

        Long deletedCount = studentService.deleteStudentByRollnoSectionStandard(rollno, standard, section);
        System.out.println("Deleted Count: " + deletedCount);
        if (deletedCount > 0) {
            return "success"; // Redirect to the students page after deleting
        } else {
            return "error"; // Redirect to an error page if no student was deleted
        }
    }

    @PostMapping("/admin/students/studentnewrecord/add")
    public String deleteStudents(@RequestBody String studentJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Student student;
        try {
            // Parse URL-encoded form data
            String[] pairs = studentJson.split("&");
            Student s = new Student();
            for (String pair : pairs) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2) {
                    String key = java.net.URLDecoder.decode(kv[0], "UTF-8");
                    String value = java.net.URLDecoder.decode(kv[1], "UTF-8");
                    switch (key) {
                        case "name":
                            s.setName(value);
                            break;
                        case "rollno":
                            s.setRollno(Integer.parseInt(value));
                            break;
                        case "age":
                            s.setAge(Integer.parseInt(value));
                            break;
                        case "address":
                            s.setAddress(value);
                            break;
                        case "phone":
                            s.setPhone(value);
                            break;
                        case "email":
                            s.setEmail(value);
                            break;
                        case "section":
                            s.setSection(value);
                            break;
                        case "standard":
                            s.setStandard(value);
                            break;
                        // Add more fields as needed
                    }
                }
            }
            student = s;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid student data format", e);
        }
        if (student.getName() == null || student.getName().isEmpty() ||
                student.getEmail() == null || student.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Missing required student fields");
        }

        try {
            studentService.saveStudent(student);
            return "success"; // Redirect to the students page after saving
        } catch (Exception ex) {
            return "error"; // Redirect to an error page if saving fails
        }
    }

    @GetMapping("/students/exams")
    public String studentsExamsList(@RequestParam("standard") String standard,
            @RequestParam("section") String section,@RequestParam("rollno") String rollno, 
            Model model) {

        if (standard == null || section == null) {
            // Optionally, handle case where teacher is not a class teacher
            model.addAttribute("error", "You are not assigned as a class teacher.");
            return "error"; // or a suitable error page
        }

        List<Exam> exams = examService.getExamsByStandardAndSection(standard, section);
        model.addAttribute("examsList", exams);
        model.addAttribute("rollno", rollno);
        System.out.println("Exams retrieved: " + exams.size());

        return "studentsexamlist";
    }

    @GetMapping("/students/exams/results")
    public String getStudentsMarks(@RequestParam("examid") String examid, @RequestParam("standard") String standard,
            @RequestParam("section") String section, @RequestParam("rollno") String rollno, Model model) {
        Long examId = Long.parseLong(examid);
        Integer rollNo = Integer.parseInt(rollno);
        System.out.println("Exam ID received: " + examId);

        Optional<Exam> exam = examService.getExamById(examId);
        if (exam == null) {
            model.addAttribute("error", "Exam not found");
            return "error"; // Redirect to an error page
        }
        model.addAttribute("selectedExam", exam.get());

        List<Subject> subjects = subjectService.getSubjectsByStandardAndSection(standard, section);
        Student studentSelected = null;
        studentSelected = studentService.findByClassSectionAndRollno(standard, section,

                rollNo);

        if (studentSelected == null) {
            model.addAttribute("error", "Student not found");
            return "error"; // Redirect to an error page
        }

        Map<Subject, Mark> subjectMarkMap = new LinkedHashMap<>();
        for (Subject subject : subjects) {
            Mark mark = markService.findByStudentExamSubject(subject, exam.get(), studentSelected);
            subjectMarkMap.put(subject, mark); // mark can be null if not found
        }

        // In your controller method
        model.addAttribute("subjectMarkMap", subjectMarkMap);
        model.addAttribute("studentName", studentSelected.getName());
        model.addAttribute("studentRollNo", studentSelected.getRollno());
        model.addAttribute("studentStandard", standard);
        model.addAttribute("studentSection", section);
        model.addAttribute("exam", exam.get());
        return "studentsresultspage";
    }

}
