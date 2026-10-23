package com.example.campus.announcement.controller;

import com.example.campus.announcement.dto.AnnouncementDTO;
import com.example.campus.announcement.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService service;

    @GetMapping
    public List<AnnouncementDTO> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public AnnouncementDTO getOne(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<AnnouncementDTO> create(@RequestBody AnnouncementDTO dto) {
        AnnouncementDTO saved = service.create(dto);
        return ResponseEntity.created(URI.create("/api/announcements/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public AnnouncementDTO update(@PathVariable Long id, @RequestBody AnnouncementDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
