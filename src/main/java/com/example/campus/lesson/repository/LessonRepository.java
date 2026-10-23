package com.example.campus.lesson.repository;

import com.example.campus.lesson.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllBySubject_Id(Long subjectId);
}