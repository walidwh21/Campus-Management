package com.example.campus.grade.service;

import com.example.campus.grade.dto.GradeDTO;
import com.example.campus.grade.model.Grade;
import com.example.campus.grade.repository.GradeRepository;
import com.example.campus.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository repository;

    public List<GradeDTO> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public GradeDTO findById(Long id) {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Grade " + id + " not found")));
    }

    public GradeDTO create(GradeDTO dto) {
        return toDto(repository.save(toEntity(dto)));
    }

    public GradeDTO update(Long id, GradeDTO dto) {
        Grade g = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Grade " + id + " not found"));
        g.setLevel(dto.getLevel());
        return toDto(repository.save(g));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new NotFoundException("Grade " + id + " not found");
        repository.deleteById(id);
    }

    /* ---------- mapping helpers ---------- */
    private GradeDTO toDto(Grade g) {
        return GradeDTO.builder()
                .id(g.getId())
                // .name(g.getName())
                .level(g.getLevel())
                .build();
    }

    private Grade toEntity(GradeDTO d) {
        return Grade.builder()
                // .name(d.getName())
                .level(d.getLevel())
                .build();
    }
}
