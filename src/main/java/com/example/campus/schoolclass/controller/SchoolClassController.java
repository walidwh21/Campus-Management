package com.example.campus.schoolclass.controller;
import com.example.campus.common.exception.NotFoundException;
import com.example.campus.schoolclass.dto.SchoolClassDTO;
import com.example.campus.schoolclass.service.SchoolClassService;
import com.example.campus.schoolclass.model.SchoolClass;
import com.example.campus.schoolclass.repository.SchoolClassRepository;
import com.example.campus.student.dto.StudentDTO;
import com.example.campus.student.service.StudentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class SchoolClassController {

    private final SchoolClassService service;
    private final SchoolClassRepository classRepo;   // NEW
    private final StudentService studentService;     // NEW

    @GetMapping
    public List<SchoolClassDTO> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public SchoolClassDTO getOne(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<SchoolClassDTO> create(@RequestBody SchoolClassDTO dto) {
        SchoolClassDTO saved = service.create(dto);
        return ResponseEntity.created(URI.create("/api/classes/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public SchoolClassDTO update(@PathVariable Long id, @RequestBody SchoolClassDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }

    /* ── placeholder sub-routes ───────────── */
    @GetMapping("/{id}/students")
    public List<StudentDTO> students(@PathVariable Long id) {
        SchoolClass sc = classRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Class " + id + " not found"));
        return sc.getStudents()
                 .stream()
                 .map(studentService::toDto)
                 .toList();
    }

    @GetMapping("/{id}/lessons")
    public List<?> lessons(@PathVariable Long id) { return List.of(); }

    @GetMapping("/{id}/events")
    public List<?> events(@PathVariable Long id) { return List.of(); }

    @GetMapping("/{id}/announcements")
    public List<?> ann(@PathVariable Long id) { return List.of(); }
}
