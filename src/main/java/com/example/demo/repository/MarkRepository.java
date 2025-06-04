package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Exam;
import com.example.demo.model.Mark;
import com.example.demo.model.Student;
import com.example.demo.model.Subject;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {
    @Query("SELECT m FROM Mark m WHERE m.subject = :subject AND m.exam = :exam AND m.student = :student")
    Mark findByStudentExamSubject(@Param("subject") Subject subject,
            @Param("exam") Exam exam,
            @Param("student") Student student);

}
