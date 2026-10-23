package com.example.campus.subject.service;

import com.example.campus.common.exception.NotFoundException;
import com.example.campus.subject.dto.SubjectDTO;
import com.example.campus.subject.model.Subject;
import com.example.campus.subject.repository.SubjectRepository;
import com.example.campus.teacher.model.Teacher;
import com.example.campus.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional          // ← every public method is now wrapped in a tx
public class SubjectService {

    private final SubjectRepository repository;
    private final TeacherRepository teacherRepo;        // ← ADD THIS

    /* ---------- CRUD ---------- */

    @Transactional(readOnly = true)
    public List<SubjectDTO> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public SubjectDTO findById(Long id) {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject " + id + " not found")));
    }

    public SubjectDTO create(SubjectDTO dto) {
        // optional uniqueness guard
        if (repository.existsByNameIgnoreCase(dto.getName())) {
            throw new IllegalArgumentException("Subject name already exists");
        }
        return toDto(repository.save(toEntity(dto)));
    }

    public SubjectDTO update(Long id, SubjectDTO dto) {
        Subject s = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject " + id + " not found"));
        s.setName(dto.getName());
        return toDto(s);          // s is managed, save() not required
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new NotFoundException("Subject " + id + " not found");
        repository.deleteById(id);
    }

    /* ---------- RELATION HELPERS ---------- */

    /** Attach an existing subject to an existing teacher */
    public void assignToTeacher(Long subjectId, Long teacherId) {
        Subject  subject  = repository.findById(subjectId)
                                 .orElseThrow(() -> new NotFoundException("Subject " + subjectId));
        Teacher  teacher  = teacherRepo.findById(teacherId)
                                 .orElseThrow(() -> new NotFoundException("Teacher " + teacherId));

        subject.getTeachers().add(teacher);   // keep both sides in sync
        teacher.getSubjects().add(subject);
        // thanks to @Transactional the change flushes automatically
    }

    /** Remove the link between a subject and a teacher */
    public void unassignFromTeacher(Long subjectId, Long teacherId) {
        Subject subject = repository.findById(subjectId)
                              .orElseThrow(() -> new NotFoundException("Subject " + subjectId));
        Teacher teacher = teacherRepo.findById(teacherId)
                              .orElseThrow(() -> new NotFoundException("Teacher " + teacherId));

        subject.getTeachers().remove(teacher);
        teacher.getSubjects().remove(subject);
    }

    /* ---------- mapping helpers ---------- */

    private SubjectDTO toDto(Subject s) {
        return SubjectDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .build();
    }

    private Subject toEntity(SubjectDTO d) {
        return Subject.builder()
                .name(d.getName())
                .build();
    }
}
