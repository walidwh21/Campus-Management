package com.example.campus.lesson.controller;

import com.example.campus.lesson.dto.LessonDTO;
import com.example.campus.lesson.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService service;

    @GetMapping
    public List<LessonDTO> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public LessonDTO getOne(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<LessonDTO> create(@RequestBody LessonDTO dto) {
        LessonDTO saved = service.create(dto);
        return ResponseEntity.created(URI.create("/api/lessons/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public LessonDTO update(@PathVariable Long id, @RequestBody LessonDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }

    /* ── placeholder sub-routes ─────────────────── */
    @GetMapping("/{id}/exams")
    public List<?> exams(@PathVariable Long id) { return List.of(); }

    @GetMapping("/{id}/assignments")
    public List<?> assignments(@PathVariable Long id) { return List.of(); }

    @GetMapping("/{id}/attendances")
    public List<?> attendances(@PathVariable Long id) { return List.of(); }
}
