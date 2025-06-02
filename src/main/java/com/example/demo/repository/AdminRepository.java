package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query("SELECT a FROM Admin a WHERE a.username = :username")
    Optional<Admin> findByUsername(@Param("username") String username);

    @Query("SELECT a FROM Admin a WHERE a.password = :password")
    Optional<Admin> findByPassword(@Param("password") String password);

    @Query("SELECT a FROM Admin a WHERE a.username = :username AND a.password = :password")
    Optional<Admin> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
