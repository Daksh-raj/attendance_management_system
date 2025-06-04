package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    // You can add custom query methods if needed
    @Query("SELECT e FROM Exam e WHERE e.standard = :standard AND e.section = :section")
    List<Exam> findByStandardAndSection(@Param("standard") String standard,
            @Param("section") String section);

    Optional<Exam> findById(@Param("examId") Long id);
}
