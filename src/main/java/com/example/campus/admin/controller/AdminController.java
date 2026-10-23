package com.example.campus.admin.controller;

import com.example.campus.admin.dto.AdminDTO;
import com.example.campus.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService service;

    @GetMapping
    public List<AdminDTO> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public AdminDTO getOne(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<AdminDTO> create(@RequestBody AdminDTO dto) {
        AdminDTO saved = service.create(dto);
        return ResponseEntity.created(URI.create("/api/admins/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public AdminDTO update(@PathVariable Long id, @RequestBody AdminDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
