package com.example.demo.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Subject;
import com.example.demo.repository.SubjectRepository;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }
    public List<Subject> getSubjectsByStandardAndSection(String standard, String section) {
        return subjectRepository.findByStandardAndSection(standard, section);
    }
    public Optional<Subject> getSubjectById(Long id) {
        return subjectRepository.findById(id);
    }
}
