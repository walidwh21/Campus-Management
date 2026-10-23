package com.example.campus.assignment.repository;

import com.example.campus.assignment.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> { }
