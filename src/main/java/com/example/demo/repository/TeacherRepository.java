package com.example.demo.repository;

import com.example.demo.model.Teacher;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    

    @Query("SELECT t FROM Teacher t WHERE t.email = :email AND t.password = :password")
    Optional<Teacher> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
}
