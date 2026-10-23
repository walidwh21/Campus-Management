package com.example.campus.student.service;
import com.example.campus.common.enums.Role;
import com.example.campus.common.enums.StudentStatus;
import com.example.campus.student.dto.StudentDTO;
import com.example.campus.student.model.Student;
import com.example.campus.student.repository.StudentRepository;
import com.example.campus.common.exception.NotFoundException;
import com.example.campus.parent.repository.ParentRepository;
import com.example.campus.schoolclass.model.SchoolClass;
import com.example.campus.schoolclass.repository.SchoolClassRepository;
import com.example.campus.grade.repository.GradeRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

import com.example.campus.common.exception.CapacityExceededException;
import com.example.campus.parent.model.Parent;


@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository repository;
     private final SchoolClassRepository classRepo; 
     private final ParentRepository parentRepo;
    private final GradeRepository      gradeRepo;
    private final BCryptPasswordEncoder encoder;


    public List<StudentDTO> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public StudentDTO findById(Long id) {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student " + id + " not found")));
    }

    public StudentDTO create(StudentDTO dto) {
        SchoolClass sc = classRepo.findById(dto.getClassId())
                              .orElseThrow(() -> new NotFoundException("Class "+dto.getClassId()+" not found"));
        if (sc.getStudents().size() >= sc.getCapacity()) {
            throw new CapacityExceededException(sc.getId(), sc.getCapacity());
        }

        if (dto.getPassword() == null || dto.getPassword().isBlank())
            throw new IllegalArgumentException("password is required");
        dto.setRole(Role.STUDENT);

        Student entity = toEntity(dto);
        Student secured = Student.withRawPassword(entity, dto.getPassword(),
                (BCryptPasswordEncoder) encoder);
        return toDto(repository.save(secured));
    }

    public StudentDTO update(Long id, StudentDTO dto) {
        Student st = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student " + id + " not found"));
        st.setFullName(dto.getFullName());
        st.setEmail(dto.getEmail());
        st.setDateOfBirth(dto.getDateOfBirth());
        st.setGender(dto.getGender());
        st.setPhone(dto.getPhone());
        st.setAddress(dto.getAddress());
        st.setEnrollmentDate(dto.getEnrollmentDate());
        st.setStatus(dto.getStatus());
        
        return toDto(repository.save(st));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new NotFoundException("Student " + id + " not found");
        repository.deleteById(id);
    }

    /* ---------- mapping helpers ---------- */
    public StudentDTO toDto(Student s) {
        return StudentDTO.builder()
                .id(s.getId())
                .fullName(s.getFullName())
                .email(s.getEmail())
                .classId(s.getSchoolClass().getId())
                .dateOfBirth(s.getDateOfBirth())
                .gender(s.getGender())
                .phone(s.getPhone())
                .address(s.getAddress())
                .enrollmentDate(s.getEnrollmentDate())
                .gradeId(s.getGrade() == null ? null : s.getGrade().getId())
                .status(s.getStatus())
                .img(s.getImg())
                .bloodType(s.getBloodType())
                .parentIds(
                    s.getParents().stream()
                    .map(Parent::getId)
                    .collect(Collectors.toSet())
                )

                .build();
    }

    private Student toEntity(StudentDTO d) {
        return Student.builder()
                .fullName(d.getFullName())
                .email(d.getEmail())
                .dateOfBirth(d.getDateOfBirth())
                .gender(d.getGender())
                .phone(d.getPhone())
                .address(d.getAddress())
                .enrollmentDate(d.getEnrollmentDate())
                .grade(
                    d.getGradeId() == null ? null
                                        : gradeRepo.findById(d.getGradeId())
                                                .orElseThrow(() ->
                                                    new NotFoundException("Grade "+d.getGradeId()+" not found"))
                )
                .status(
                    d.getStatus() != null ? d.getStatus()
                                        : StudentStatus.ACTIVE)
                .parents(resolveParents(d.getParentIds()))
                .schoolClass(
                        classRepo.findById(d.getClassId())
                          .orElseThrow(() -> new NotFoundException("Class "+d.getClassId()+" not found"))
                )
                .img(d.getImg())
                .bloodType(d.getBloodType())
                .build();

    }
//Helpers 

private Set<Parent> resolveParents(Set<Long> ids) {
    if (ids == null || ids.isEmpty()) return new HashSet<>();
    return ids.stream()
              .map(id -> parentRepo.findById(id)
                     .orElseThrow(() -> new NotFoundException("Parent "+id+" not found")))
              .collect(Collectors.toSet());
}
}
