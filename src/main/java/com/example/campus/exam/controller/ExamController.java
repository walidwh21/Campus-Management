package com.example.campus.exam.controller;

import com.example.campus.exam.dto.ExamDTO;
import com.example.campus.exam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService service;

    @GetMapping
    public List<ExamDTO> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public ExamDTO getOne(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<ExamDTO> create(@RequestBody ExamDTO dto) {
        ExamDTO saved = service.create(dto);
        return ResponseEntity.created(URI.create("/api/exams/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ExamDTO update(@PathVariable Long id, @RequestBody ExamDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }

    /* ── placeholder sub-route ─────────── */
    @GetMapping("/{id}/results")
    public List<?> results(@PathVariable Long id) { return List.of(); }
}
