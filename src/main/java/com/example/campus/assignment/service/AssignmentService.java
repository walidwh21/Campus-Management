package com.example.campus.assignment.service;

import com.example.campus.common.exception.NotFoundException;
import com.example.campus.assignment.dto.AssignmentDTO;
import com.example.campus.assignment.model.Assignment;
import com.example.campus.assignment.repository.AssignmentRepository;
import com.example.campus.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository repository;
    private final LessonRepository lessonRepo;

    public List<AssignmentDTO> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public AssignmentDTO findById(Long id) {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assignment " + id + " not found")));
    }

    public AssignmentDTO create(AssignmentDTO dto) { return toDto(repository.save(toEntity(dto))); }

    public AssignmentDTO update(Long id, AssignmentDTO dto) {
        Assignment a = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assignment " + id + " not found"));
        a.setTitle(dto.getTitle());
        a.setDueDate(dto.getDueDate());
        a.setLesson(lessonRepo.findById(dto.getLessonId())
                .orElseThrow(() -> new NotFoundException("Lesson " + dto.getLessonId() + " not found")));
        return toDto(repository.save(a));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new NotFoundException("Assignment " + id + " not found");
        repository.deleteById(id);
    }

    /* ---------- mapping helpers ---------- */
    private AssignmentDTO toDto(Assignment a) {
        return AssignmentDTO.builder()
                .id(a.getId())
                .title(a.getTitle())
                .dueDate(a.getDueDate())
                .lessonId(a.getLesson().getId())
                .build();
    }

    private Assignment toEntity(AssignmentDTO d) {
        return Assignment.builder()
                .title(d.getTitle())
                .dueDate(d.getDueDate())
                .lesson(lessonRepo.findById(d.getLessonId())
                        .orElseThrow(() -> new NotFoundException("Lesson " + d.getLessonId() + " not found")))
                .build();
    }
}
