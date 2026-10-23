package com.example.campus.event.controller;

import com.example.campus.event.dto.EventDTO;
import com.example.campus.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    @GetMapping
    public List<EventDTO> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public EventDTO getOne(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<EventDTO> create(@RequestBody EventDTO dto) {
        EventDTO saved = service.create(dto);
        return ResponseEntity.created(URI.create("/api/events/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public EventDTO update(@PathVariable Long id, @RequestBody EventDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
