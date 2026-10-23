package com.example.campus.parent.controller;

import com.example.campus.parent.dto.ParentDTO;
import com.example.campus.parent.service.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/parents")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService service;

    @GetMapping
    public List<ParentDTO> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public ParentDTO getOne(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<ParentDTO> create(@RequestBody ParentDTO dto) {
        ParentDTO saved = service.create(dto);
        return ResponseEntity.created(URI.create("/api/parents/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ParentDTO update(@PathVariable Long id, @RequestBody ParentDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
