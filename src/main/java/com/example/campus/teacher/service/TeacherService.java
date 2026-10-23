package com.example.campus.teacher.service;

import com.example.campus.teacher.dto.TeacherDTO;
import com.example.campus.teacher.model.Teacher;
import com.example.campus.teacher.repository.TeacherRepository;
import com.example.campus.common.enums.Role;
import com.example.campus.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository repository;
    private final PasswordEncoder   encoder;

    public List<TeacherDTO> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public TeacherDTO findById(Long id) {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher " + id + " not found")));
    }

    public TeacherDTO create(TeacherDTO dto) {
    if (dto.getPassword() == null || dto.getPassword().isBlank())
        throw new IllegalArgumentException("password is required");
        Teacher saved = repository.save(
                Teacher.builder()
                       .fullName(dto.getFullName())
                       .email(dto.getEmail())
                       .password(encoder.encode(dto.getPassword()))   
                       .role(Role.TEACHER)
                       .build());
        return toDto(saved);
    }

    public TeacherDTO update(Long id, TeacherDTO dto) {
        Teacher t = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher " + id + " not found"));
        t.setFullName(dto.getFullName());
        t.setEmail(dto.getEmail());
        return toDto(repository.save(t));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new NotFoundException("Teacher " + id + " not found");
        repository.deleteById(id);
    }
    public List<TeacherDTO> findBySubject(Long subjectId) {
        List<Teacher> teachers = repository.findAllBySubjects_Id(subjectId);
        if (teachers.isEmpty()) {
            throw new NotFoundException("No teachers found for subject " + subjectId);
        }
        return teachers.stream().map(this::toDto).toList();
    }
    /* ---------- mapping helpers ---------- */
    private TeacherDTO toDto(Teacher t) {
        return TeacherDTO.builder()
                .id(t.getId())
                .fullName(t.getFullName())
                .email(t.getEmail())
                .build();
    }

    private Teacher toEntity(TeacherDTO d) {
        return Teacher.builder()
                .fullName(d.getFullName())
                .email(d.getEmail())
                .role(Role.TEACHER)
                .build();
    }
}
