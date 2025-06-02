package com.example.demo.service;

import com.example.demo.model.Attendance;
import com.example.demo.model.Student;
import com.example.demo.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public Attendance getAttendanceByStudentAndDate(Student student, LocalDate date) {
        return attendanceRepository.findByStudentAndDate(student, date);
    }

    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceForMonth(Optional<Student> student, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        return attendanceRepository.findByStudentAndDateBetween(student, start, end);
    }
    public void deleteAttendance(Attendance attendance) {
        attendanceRepository.delete(attendance);
    }

    public void deleteAttendanceByStudent(Student student) {
        attendanceRepository.deleteByStudent(student);
    }
}

