package com.example.campus.student.repository;

import com.example.campus.student.model.Student;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> { 

    Optional<Student> findByEmail(String email);

}
