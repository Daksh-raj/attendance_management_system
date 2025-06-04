package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

import jakarta.transaction.Transactional;

import com.example.demo.repository.AttendanceRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired  
    private AttendanceService attendanceService;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findByClassSectionAndRollno(String standard, String section, Integer rollno) {
        return studentRepository.findByClassSectionAndRollno(standard, section, rollno);
    }

    public List<Student> getStudentsByClassAndSection(String standard, String section) {
        return studentRepository.findByClassAndSection(standard, section);
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    

    public Long deleteStudentByRollnoSectionStandard(Integer rollno, String standard, String section) {
        // Find the student first
        System.out.println("Attempting to delete student with rollno: " + rollno + ", standard: " + standard + ", section: " + section);
        Student student = studentRepository.findByRollnoAndStandardAndSection(rollno, standard, section)
                .orElse(null);

        if (student == null) {
            return 0L;
        }

        System.out.println("Deleting student: " + student);

        // Delete all attendance records for this student
        attendanceService.deleteAttendanceByStudent(student);

        // Now delete the student
        studentRepository.delete(student);

        return 1L; // 1 student deleted
    }

    @Transactional
    public void updateStudentInfo(String address, Integer age, String phone, String email,
                                  String standard, String section, Integer rollno) {
        studentRepository.updateStudentInfo(address, age, phone, email, standard, section, rollno);
    }

}
