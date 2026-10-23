package com.example.campus.teacher.controller;

// import com.example.campus.security.roles.IsAdmin;
import com.example.campus.security.roles.IsAdminOrTeacher;
// import com.example.campus.security.roles.IsTeacher;
import com.example.campus.teacher.dto.TeacherDTO;
import com.example.campus.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService service;
    @IsAdminOrTeacher    
    @GetMapping
    public List<TeacherDTO> getAll() { return service.findAll(); }

    @IsAdminOrTeacher    
    @GetMapping("/{id}")
    public TeacherDTO getOne(@PathVariable Long id) { return service.findById(id); }

    @IsAdminOrTeacher    
    @PostMapping
    public ResponseEntity<TeacherDTO> create(@RequestBody TeacherDTO dto) {
        TeacherDTO saved = service.create(dto);
        return ResponseEntity.created(URI.create("/api/teachers/" + saved.getId())).body(saved);
    }

    @IsAdminOrTeacher    
    @PutMapping("/{id}")
    public TeacherDTO update(@PathVariable Long id, @RequestBody TeacherDTO dto) {
        return service.update(id, dto);
    }

    @IsAdminOrTeacher    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
