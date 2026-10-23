package com.example.campus.assignment.controller;

import com.example.campus.assignment.dto.AssignmentDTO;
import com.example.campus.assignment.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService service;

    @GetMapping
    public List<AssignmentDTO> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public AssignmentDTO getOne(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<AssignmentDTO> create(@RequestBody AssignmentDTO dto) {
        AssignmentDTO saved = service.create(dto);
        return ResponseEntity.created(URI.create("/api/assignments/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public AssignmentDTO update(@PathVariable Long id, @RequestBody AssignmentDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }

    /* ── placeholder sub-route ───────────── */
    @GetMapping("/{id}/results")
    public List<?> results(@PathVariable Long id) { return List.of(); }
}
