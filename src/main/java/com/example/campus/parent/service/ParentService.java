package com.example.campus.parent.service;
import java.util.Set;
import java.util.stream.Collectors;
import com.example.campus.student.model.Student;

import com.example.campus.parent.dto.ParentDTO;
import com.example.campus.parent.model.Parent;
import com.example.campus.parent.repository.ParentRepository;
import com.example.campus.student.repository.StudentRepository;
// import com.example.campus.teacher.model.Teacher;
import com.example.campus.common.enums.Role;
import com.example.campus.common.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository repository;
    private final StudentRepository studentRepo;
    private final PasswordEncoder encoder;

    public List<ParentDTO> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public ParentDTO findById(Long id) {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Parent " + id + " not found")));
    }

    public ParentDTO create(ParentDTO dto) {
    if (dto.getPassword() == null || dto.getPassword().isBlank())
        throw new IllegalArgumentException("password is required");
    Parent t = toEntity(dto);
    t.setPassword(encoder.encode(dto.getPassword()));
    t.setRole(Role.PARENT);
    return toDto(repository.save(t));
    }

    public ParentDTO update(Long id, ParentDTO dto) {
        Parent p = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Parent " + id + " not found"));
        p.setFullName(dto.getFullName());
        p.setEmail(dto.getEmail());
        return toDto(repository.save(p));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new NotFoundException("Parent " + id + " not found");
        repository.deleteById(id);
    }

    private ParentDTO toDto(Parent p) {
        return ParentDTO.builder()
                .id(p.getId())
                .fullName(p.getFullName())
                .email(p.getEmail())
                .childIds(
                    p.getChildren().stream()
                        .map(Student::getId)
                        .collect(Collectors.toSet())
                )
                .build();
    }

    private Parent toEntity(ParentDTO d) {
        return Parent.builder()
                .fullName(d.getFullName())
                .email(d.getEmail())
                .role(Role.PARENT)
                .children(resolveStudents(d.getChildIds()))
                .build();
    }


/* ---------- helpers ---------- */
private Set<Student> resolveStudents(Set<Long> ids) {
   if (ids == null || ids.isEmpty()) return new HashSet<>();
   return ids.stream()
             .map(id -> studentRepo.findById(id)
                     .orElseThrow(() -> new NotFoundException("Student "+id+" not found")))
             .collect(Collectors.toSet());
}
}
