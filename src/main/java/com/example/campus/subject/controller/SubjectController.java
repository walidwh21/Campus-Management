package com.example.campus.subject.controller;

import com.example.campus.subject.dto.SubjectDTO;
import com.example.campus.subject.service.SubjectService;
import com.example.campus.teacher.dto.TeacherDTO;
import com.example.campus.teacher.service.TeacherService;
import com.example.campus.lesson.dto.LessonDTO;
import com.example.campus.lesson.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService service;
    private final TeacherService teacherSvc;   // ← ADD
    private final LessonService  lessonSvc;    // ← ADD

    /* ---------- CRUD ---------- */

    @GetMapping
    public List<SubjectDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public SubjectDTO getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<SubjectDTO> create(@RequestBody SubjectDTO dto) {
        SubjectDTO saved = service.create(dto);
        return ResponseEntity
                .created(URI.create("/api/subjects/" + saved.getId()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public SubjectDTO update(@PathVariable Long id,
                             @RequestBody SubjectDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    /* ---------- sub-routes ---------- */

    /**
     * GET /api/subjects/{id}/teachers  
     * returns all teachers who teach the subject
     */
    @GetMapping("/{id}/teachers")
    public List<TeacherDTO> teachers(@PathVariable Long id) {
        return teacherSvc.findBySubject(id);       // implemented in TeacherService
    }

    /**
     * GET /api/subjects/{id}/lessons  
     * returns all lessons mapped to the subject
     */
    @GetMapping("/{id}/lessons")
    public List<LessonDTO> lessons(@PathVariable Long id) {
        return lessonSvc.findBySubject(id);        // implemented in LessonService
    }
}
