package com.example.campus.grade.controller;

import com.example.campus.grade.dto.GradeDTO;
import com.example.campus.grade.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService service;

    @GetMapping
    public List<GradeDTO> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public GradeDTO getOne(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<GradeDTO> create(@RequestBody GradeDTO dto) {
        GradeDTO saved = service.create(dto);
        return ResponseEntity.created(URI.create("/api/grades/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public GradeDTO update(@PathVariable Long id, @RequestBody GradeDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }

    /* ── placeholder routes required by spec ───────── */
    @GetMapping("/{id}/students")
    public List<?> studentsInGrade(@PathVariable Long id) { return List.of(); }

    @GetMapping("/{id}/classes")
    public List<?> classesInGrade(@PathVariable Long id) { return List.of(); }
}
