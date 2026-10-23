package com.example.campus.attendance.service;

import com.example.campus.common.exception.NotFoundException;
import com.example.campus.attendance.dto.AttendanceDTO;
import com.example.campus.attendance.model.Attendance;
import com.example.campus.attendance.repository.AttendanceRepository;
import com.example.campus.student.repository.StudentRepository;
import com.example.campus.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository repository;
    private final StudentRepository studentRepo;
    private final LessonRepository lessonRepo;

    public List<AttendanceDTO> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public AttendanceDTO findById(Long id) {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Attendance " + id + " not found")));
    }

    public AttendanceDTO create(AttendanceDTO dto) { return toDto(repository.save(toEntity(dto))); }

    public AttendanceDTO update(Long id, AttendanceDTO dto) {
        Attendance a = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Attendance " + id + " not found"));
        a.setStatus(dto.getStatus());
        a.setStudent(studentRepo.findById(dto.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student " + dto.getStudentId() + " not found")));
        a.setLesson(lessonRepo.findById(dto.getLessonId())
                .orElseThrow(() -> new NotFoundException("Lesson " + dto.getLessonId() + " not found")));
        return toDto(repository.save(a));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new NotFoundException("Attendance " + id + " not found");
        repository.deleteById(id);
    }

    /* ---------- helpers ---------- */
    private AttendanceDTO toDto(Attendance a) {
        return AttendanceDTO.builder()
                .id(a.getId())
                .status(a.getStatus())
                .studentId(a.getStudent().getId())
                .lessonId(a.getLesson().getId())
                .build();
    }

    private Attendance toEntity(AttendanceDTO d) {
        return Attendance.builder()
                .status(d.getStatus())
                .student(studentRepo.findById(d.getStudentId())
                        .orElseThrow(() -> new NotFoundException("Student " + d.getStudentId() + " not found")))
                .lesson(lessonRepo.findById(d.getLessonId())
                        .orElseThrow(() -> new NotFoundException("Lesson " + d.getLessonId() + " not found")))
                .build();
    }
}
