package com.example.campus.grade.repository;

import com.example.campus.grade.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {   boolean existsByLevel(Integer level); }
