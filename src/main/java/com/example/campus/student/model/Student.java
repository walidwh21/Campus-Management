package com.example.campus.student.model;
import com.example.campus.common.audit.Auditable;
import com.example.campus.common.enums.Gender;
import com.example.campus.common.enums.Role;
import com.example.campus.common.enums.StudentStatus;
import com.example.campus.grade.model.Grade;

import java.time.LocalDate;
import com.example.campus.schoolclass.model.SchoolClass;
import jakarta.persistence.*;
import lombok.*;
import com.example.campus.parent.model.Parent;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Student extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;      // “name” in Prisma
    private String surname;       // NEW
    
    @Column(unique = true)
    private String username;    
    private String email;
// Student.java
    private String password;                  // nullable
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;
    /* convenience helper when creating with builder */
    public static Student withRawPassword(Student s, String rawPw, BCryptPasswordEncoder enc){
        s.setPassword(enc.encode(rawPw));
        s.setRole(Role.STUDENT);
        return s;
    }
     
         /* ─────────── NEW FIELDS ─────────── */
     
         /** Date of birth (required for age checks) */
         @Column(nullable = false)
         private LocalDate dateOfBirth;
     
         /** MALE / FEMALE / OTHER */
         @Enumerated(EnumType.STRING)
         @Column(nullable = false, length = 10)
         private Gender gender;
     
         /** Contact phone in E.164 format – optional */
         private String phone;
     
         /** Full postal address – optional, 255 chars max */
         @Column(length = 255)
         private String address;
     
         /** When the student first enrolled */
         private LocalDate enrollmentDate;
     
         /** ACTIVE / GRADUATED / INACTIVE */
         @Enumerated(EnumType.STRING)
         @Column(nullable = false, length = 15)
         private StudentStatus status;
     
         /** Soft-delete flag */
         @Builder.Default
         @Column(nullable = false)
         private boolean deleted = false;    

        @Column(length = 255)
        private String img;  

        @Column(length = 5)
        private String bloodType; 

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id") 
    private SchoolClass schoolClass;

    @Builder.Default
    @ManyToMany(mappedBy = "children")
    private Set<Parent> parents = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Grade grade;
}
