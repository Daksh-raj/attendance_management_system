package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Exam;
import com.example.demo.repository.ExamRepository;
import java.util.Optional;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    public Exam saveExam(Exam exam) {
        return examRepository.save(exam);
    }

    public List<Exam> getExamsByStandardAndSection(String standard, String section) {
        return examRepository.findByStandardAndSection(standard, section);
    }

    public Optional<Exam> getExamById(Long id) {
        return examRepository.findById(id);
    }   
}
