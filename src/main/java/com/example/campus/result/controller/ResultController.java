package com.example.campus.result.controller;

import com.example.campus.result.dto.ResultDTO;
import com.example.campus.result.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService service;

    @GetMapping
    public List<ResultDTO> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResultDTO getOne(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<ResultDTO> create(@RequestBody ResultDTO dto) {
        ResultDTO saved = service.create(dto);
        return ResponseEntity.created(URI.create("/api/results/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResultDTO update(@PathVariable Long id, @RequestBody ResultDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
