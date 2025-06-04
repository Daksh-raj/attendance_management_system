package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Admin;
import com.example.demo.model.Student;
import com.example.demo.service.AdminService;
import com.example.demo.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Controller;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;

    @GetMapping("/admin/login")
    public String showAdminLoginPage() {
        System.out.println("Admin login page accessed");
        return "adminloginpage"; // This will render src/main/resources/templates/adminlogin.html
    }

    @PostMapping("/admin/login")
    public String handleAdminLogin(@RequestBody String formData, Model model, HttpSession session) {
        try {
            String[] pairs = formData.split("&");
            String username = null, password = null;
            for (String pair : pairs) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2) {
                    String key = java.net.URLDecoder.decode(kv[0], "UTF-8");
                    String value = java.net.URLDecoder.decode(kv[1], "UTF-8");
                    if ("username".equals(key)) {
                        username = value;
                    } else if ("password".equals(key)) {
                        password = value;
                    }
                }
            }
            Optional<Admin> admin = adminService.getAdminByUsernameAndPassword(username, password);
            if (admin.isEmpty()) {
                model.addAttribute("error", "Invalid username or password");
                return "error";
            }
            Admin adminObj = admin.get();
            String adminUsername = adminObj.getUsername();
            String adminPassword = adminObj.getPassword();
            if (adminUsername.equals(username) && adminPassword.equals(password)) {
                session.setAttribute("loggedInAdmin", adminObj);
                model.addAttribute("loggedInAdmin", adminObj);
                return "redirect:/admin";
            } else {
                model.addAttribute("error", "Invalid username or password");
                return "error";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Login error");
            return "error";
        }
    }

    @GetMapping("/admin/logout")
    public String handleAdminLogout(HttpSession session) {
        session.removeAttribute("loggedInAdmin");
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/admin/students/studentupdaterecord")
    public String adminStudentUpdateRecord() {
        return "adminstudentspage"; // This will render src/main/resources/templates/students.html
    }

    @GetMapping("/admin/students/studentupdaterecord/update")
    public String updateStudentByClassSectionAndRollno(
            @RequestParam("rollno") String rollNo,
            @RequestParam("standard") String standard,
            @RequestParam("section") String section,
            Model model) {
        Integer rollno = Integer.parseInt(rollNo);
        try {
            if (standard == null || section == null || rollno == null) {
                System.out.println("Missing required fields: standard, section, or rollno");
                model.addAttribute("error", "Missing required fields");
                return "error";
            }
            Student updateStudent = null;
            updateStudent = studentService.findByClassSectionAndRollno(standard, section,

                    rollno);
            model.addAttribute("updateStudent", updateStudent);
            System.out.println("single Student: " + updateStudent);
        } catch (Exception e) {
            model.addAttribute("error", "Error occurred while fetching student");
            return "error";
        }
        return "studentupdaterecord";
    }

    @PostMapping("/admin/students/studentupdaterecord/update")
    public String adminStudentUpdate(@RequestBody String studentJson) {
        String name = null;
        Integer rollno = null;
        Integer age = null;
        String address = null;
        String phone = null;
        String email = null;
        String section = null;
        String standard = null;
        try {
            // Parse URL-encoded form data
            String[] pairs = studentJson.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2) {
                    String key = java.net.URLDecoder.decode(kv[0], "UTF-8");
                    String value = java.net.URLDecoder.decode(kv[1], "UTF-8");
                    switch (key) {
                        case "name":
                            name=value;
                            break;
                        case "rollno":
                            rollno = Integer.parseInt(value);
                            break;
                        case "age":
                            age= Integer.parseInt(value);
                            break;
                        case "address":
                            address = value;
                            break;
                        case "phone":
                            phone = value;
                            break;
                        case "email":
                            email = value;
                            break;
                        case "section":
                            section = value;
                            break;
                        case "standard":
                            standard = value;
                            break;
                        // Add more fields as needed
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid student data format", e);
        }
        try{
            studentService.updateStudentInfo(address, age, phone, email, standard, section, rollno);
            return "success"; // Return a success page or message
        }

        catch (Exception e) {
            System.out.println("Error occurred while updating student: " + e.getMessage());
            return "error"; // Return an error page or message
        }
    }
}