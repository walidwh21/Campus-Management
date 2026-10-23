package com.example.campus.attendance.controller;

import com.example.campus.attendance.dto.AttendanceDTO;
import com.example.campus.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService service;

    @GetMapping
    public List<AttendanceDTO> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public AttendanceDTO getOne(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<AttendanceDTO> create(@RequestBody AttendanceDTO dto) {
        AttendanceDTO saved = service.create(dto);
        return ResponseEntity.created(URI.create("/api/attendances/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public AttendanceDTO update(@PathVariable Long id, @RequestBody AttendanceDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
