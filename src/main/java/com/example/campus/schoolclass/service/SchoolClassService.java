package com.example.campus.schoolclass.service;

import com.example.campus.common.exception.NotFoundException;
import com.example.campus.grade.repository.GradeRepository;
import com.example.campus.schoolclass.dto.SchoolClassDTO;
import com.example.campus.schoolclass.model.SchoolClass;
import com.example.campus.schoolclass.repository.SchoolClassRepository;
import com.example.campus.teacher.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepository repository;
    private final GradeRepository gradeRepo;
    private final TeacherRepository teacherRepo;


    public List<SchoolClassDTO> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public SchoolClassDTO findById(Long id) {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Class " + id + " not found")));
    }

    public SchoolClassDTO create(SchoolClassDTO dto) {
        return toDto(repository.save(toEntity(dto)));
    }

    public SchoolClassDTO update(Long id, SchoolClassDTO dto) {
        SchoolClass c = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Class " + id + " not found"));
        c.setName(dto.getName());
        c.setGrade(gradeRepo.findById(dto.getGradeId())
                .orElseThrow(() -> new NotFoundException("Grade " + dto.getGradeId() + " not found")));
                c.setCapacity(dto.getCapacity());
                c.setSupervisor(dto.getSupervisorId() == null ? null :
                        teacherRepo.findById(dto.getSupervisorId())
                                   .orElseThrow(() -> new NotFoundException("Teacher "+dto.getSupervisorId()+" not found")));
        return toDto(repository.save(c));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new NotFoundException("Class " + id + " not found");
        repository.deleteById(id);
    }

    /* ---------- helpers ---------- */
    public  SchoolClassDTO toDto(SchoolClass c) {
        return SchoolClassDTO.builder()
                .id(c.getId())
                .name(c.getName())
                .gradeId(c.getGrade().getId())
                .capacity(c.getCapacity())
                .supervisorId(c.getSupervisor() == null ? null : c.getSupervisor().getId())                
                .build();
    }

    public SchoolClass toEntity(SchoolClassDTO d) {
        return SchoolClass.builder()
                .name(d.getName())
                .grade(gradeRepo.findById(d.getGradeId())
                        .orElseThrow(() -> new NotFoundException("Grade " + d.getGradeId() + " not found")))
                .capacity(
                        d.getCapacity() != null ? d.getCapacity() : 30
                    )
                    .supervisor(
                        d.getSupervisorId() == null ? null
                                                    : teacherRepo.findById(d.getSupervisorId())
                                                                .orElseThrow(() -> new NotFoundException("Teacher "+d.getSupervisorId()+" not found"))
                    )
                .build();
    }
}
