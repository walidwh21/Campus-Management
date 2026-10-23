package com.example.campus.result.service;

import com.example.campus.common.exception.NotFoundException;
import com.example.campus.result.dto.ResultDTO;
import com.example.campus.result.model.Result;
import com.example.campus.result.repository.ResultRepository;
import com.example.campus.student.repository.StudentRepository;
import com.example.campus.exam.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepository repository;
    private final StudentRepository studentRepo;
    private final ExamRepository examRepo;

    public List<ResultDTO> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public ResultDTO findById(Long id) {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Result " + id + " not found")));
    }

    public ResultDTO create(ResultDTO dto) { return toDto(repository.save(toEntity(dto))); }

    public ResultDTO update(Long id, ResultDTO dto) {
        Result r = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Result " + id + " not found"));
        r.setScore(dto.getScore());
        r.setStudent(studentRepo.findById(dto.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student " + dto.getStudentId() + " not found")));
        r.setExam(examRepo.findById(dto.getExamId())
                .orElseThrow(() -> new NotFoundException("Exam " + dto.getExamId() + " not found")));
        return toDto(repository.save(r));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new NotFoundException("Result " + id + " not found");
        repository.deleteById(id);
    }

    /* ---------- helpers ---------- */
    private ResultDTO toDto(Result r) {
        return ResultDTO.builder()
                .id(r.getId())
                .score(r.getScore())
                .studentId(r.getStudent().getId())
                .examId(r.getExam().getId())
                .build();
    }

    private Result toEntity(ResultDTO d) {
        return Result.builder()
                .score(d.getScore())
                .student(studentRepo.findById(d.getStudentId())
                        .orElseThrow(() -> new NotFoundException("Student " + d.getStudentId() + " not found")))
                .exam(examRepo.findById(d.getExamId())
                        .orElseThrow(() -> new NotFoundException("Exam " + d.getExamId() + " not found")))
                .build();
    }
}
