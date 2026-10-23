package com.example.campus.student.dto;

import java.time.LocalDate;
import java.util.Set;

import com.example.campus.common.enums.Gender;
import com.example.campus.common.enums.Role;
import com.example.campus.common.enums.StudentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public class StudentDTO {
        private Long id;
        private String fullName;
        private String email;
        private LocalDate dateOfBirth;
        private Gender gender;
        private String phone;
        private String address;
        private LocalDate enrollmentDate;
        private StudentStatus status;
        private Long classId;
        private Long gradeId;
        private Set<Long> parentIds;
        private String img;
        private String bloodType;
        @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
        private String password;
        private Role   role;
}
