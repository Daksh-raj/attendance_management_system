
package com.example.demo.repository;

import com.example.demo.model.Attendance;
import com.example.demo.model.Student;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query("SELECT a FROM Attendance a WHERE a.student = :student AND a.date = :date")
    Attendance findByStudentAndDate(@Param("student") Student student, @Param("date") java.time.LocalDate date);

    List<Attendance> findByStudentAndDateBetween(Optional<Student> student, LocalDate start, LocalDate end);

    void delete(Attendance attendance);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM Attendance a WHERE a.student = :student")
    void deleteByStudent(Student student);
}

