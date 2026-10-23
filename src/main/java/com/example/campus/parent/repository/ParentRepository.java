package com.example.campus.parent.repository;

import com.example.campus.parent.model.Parent;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent, Long> { 
    Optional<Parent> findByEmail(String email); 
}