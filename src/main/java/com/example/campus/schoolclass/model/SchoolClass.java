package com.example.campus.schoolclass.model;

import java.util.ArrayList;
import java.util.List;

import com.example.campus.grade.model.Grade;
import com.example.campus.student.model.Student;
import com.example.campus.teacher.model.Teacher;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SchoolClass {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @Column(nullable = false)
    private Integer capacity = 30;


    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher supervisor;

    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Grade grade;


    @Builder.Default        
    @OneToMany(mappedBy = "schoolClass",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Student> students = new ArrayList<>();
}

