package com.example.campus.exam.service;

import com.example.campus.common.exception.NotFoundException;
import com.example.campus.exam.dto.ExamDTO;
import com.example.campus.exam.model.Exam;
import com.example.campus.exam.repository.ExamRepository;
import com.example.campus.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository repository;
    private final LessonRepository lessonRepo;

    public List<ExamDTO> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public ExamDTO findById(Long id) {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exam " + id + " not found")));
    }

    public ExamDTO create(ExamDTO dto) { return toDto(repository.save(toEntity(dto))); }

    public ExamDTO update(Long id, ExamDTO dto) {
        Exam e = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exam " + id + " not found"));
        e.setTitle(dto.getTitle());
        e.setExamDate(dto.getExamDate());
        e.setLesson(lessonRepo.findById(dto.getLessonId())
                .orElseThrow(() -> new NotFoundException("Lesson " + dto.getLessonId() + " not found")));
        return toDto(repository.save(e));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new NotFoundException("Exam " + id + " not found");
        repository.deleteById(id);
    }

    /* ---------- mapping helpers ---------- */
    private ExamDTO toDto(Exam e) {
        return ExamDTO.builder()
                .id(e.getId())
                .title(e.getTitle())
                .examDate(e.getExamDate())
                .lessonId(e.getLesson().getId())
                .build();
    }

    private Exam toEntity(ExamDTO d) {
        return Exam.builder()
                .title(d.getTitle())
                .examDate(d.getExamDate())
                .lesson(lessonRepo.findById(d.getLessonId())
                        .orElseThrow(() -> new NotFoundException("Lesson " + d.getLessonId() + " not found")))
                .build();
    }
}
