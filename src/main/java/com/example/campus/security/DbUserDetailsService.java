package com.example.campus.security;

import com.example.campus.admin.repository.AdminRepository;
import com.example.campus.teacher.repository.TeacherRepository;
import com.example.campus.parent.repository.ParentRepository;
import com.example.campus.student.repository.StudentRepository;
import com.example.campus.common.enums.Role;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DbUserDetailsService implements UserDetailsService {

    private final AdminRepository   admins;
    private final TeacherRepository teachers;
    private final ParentRepository  parents;
    private final StudentRepository students;
    private final PasswordEncoder   encoder;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return admins.findByEmail(email).map(this::map)
            .orElseGet(() -> teachers.findByEmail(email).map(this::map)
            .orElseGet(() -> parents.findByEmail(email).map(this::map)
            .orElseGet(() -> students.findByEmail(email).map(this::map)
            .orElseThrow(() -> new UsernameNotFoundException(email)))));
    }


    private UserDetails map(Object u) {
        String pw; Role role;
        if (u instanceof com.example.campus.admin.model.Admin a)         { pw = a.getPassword(); role = a.getRole(); }
        else if (u instanceof com.example.campus.teacher.model.Teacher t){ pw = t.getPassword(); role = t.getRole(); }
        else if (u instanceof com.example.campus.parent.model.Parent p)  { pw = p.getPassword(); role = p.getRole(); }
        else                                                     { pw = ((com.example.campus.student.model.Student)u).getPassword();
                                                                    role = ((com.example.campus.student.model.Student)u).getRole(); }

        return User.withUsername(getEmail(u))
                   .password(pw)
                   .authorities(role.asAuthority())
                   .build();
    }

 private String getEmail(Object o) {
     if (o instanceof com.example.campus.admin.model.Admin a) {
         return a.getEmail();
     } else if (o instanceof com.example.campus.teacher.model.Teacher t) {
         return t.getEmail();
     } else if (o instanceof com.example.campus.parent.model.Parent p) {
         return p.getEmail();
     } else { // Student
         return ((com.example.campus.student.model.Student) o).getEmail();
     }
 }
}
