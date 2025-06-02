package com.example.demo.service;

import com.example.demo.model.Teacher;
import com.example.demo.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }
    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
    public Optional<Teacher> findByEmailAndPassword(String email, String password) {
        return teacherRepository.findByEmailAndPassword(email, password);
    }
}
