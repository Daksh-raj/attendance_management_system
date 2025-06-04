package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.*;
import com.example.demo.repository.*;

import com.example.demo.repository.MarkRepository;
import com.example.demo.model.Mark;
import java.util.Optional;


@Service
public class MarkService {

    @Autowired
    private MarkRepository markRepository;  
    public void saveMark(Mark mark) {
        markRepository.save(mark);
    }
    public Mark findByStudentExamSubject(Subject subject, Exam exam, Student student)
    {
        return markRepository.findByStudentExamSubject(subject,exam,student);
    }
}
