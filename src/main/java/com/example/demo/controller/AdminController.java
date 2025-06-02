package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.model.Admin;
import com.example.demo.service.AdminService;
import org.springframework.stereotype.Controller;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

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
}