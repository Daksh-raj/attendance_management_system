package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s FROM Student s WHERE s.name = :name")
    Optional<Student> findByName(@Param("name") String name);

    @Query("SELECT s FROM Student s WHERE s.rollno = :rollno")
    Optional<Student> findByRollno(@Param("rollno") Integer rollno);

    @Query("SELECT s FROM Student s WHERE s.age = :age")
    List<Student> findByAge(@Param("age") int age);

    @Query("SELECT s FROM Student s WHERE s.address = :address")
    List<Student> findByAddress(@Param("address") String address);

    @Query("SELECT s FROM Student s WHERE s.standard = :standard AND s.section = :section AND s.rollno = :rollno")
    Student findByClassSectionAndRollno(@Param("standard") String standard, @Param("section") String section,
            @Param("rollno") Integer rollno);

    @Query("SELECT s FROM Student s WHERE s.standard = :standard AND s.section = :section")
    List<Student> findByClassAndSection(@Param("standard") String standard, @Param("section") String section);

    @Query("SELECT s FROM Student s WHERE s.id = :id")
    Optional<Student> findById(@Param("id") Long id);

    @Query("SELECT s FROM Student s WHERE s.rollno = :rollno AND s.standard = :standard AND s.section = :section")
    Optional<Student> findByRollnoAndStandardAndSection(Integer rollno, String standard, String section);

    @Modifying
    @Query("UPDATE Student s SET s.address = :address, s.age = :age, s.phone = :phone, s.email = :email " +
            "WHERE s.standard = :standard AND s.section = :section AND s.rollno = :rollno")
    void updateStudentInfo(@Param("address") String address,
            @Param("age") Integer age,
            @Param("phone") String phone,
            @Param("email") String email,
            @Param("standard") String standard,
            @Param("section") String section,
            @Param("rollno") Integer rollno);

}
