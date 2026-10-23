package com.example.campus.admin.service;

import com.example.campus.admin.dto.AdminDTO;
import com.example.campus.admin.model.Admin;
import com.example.campus.admin.repository.AdminRepository;
import com.example.campus.common.enums.Role;
import com.example.campus.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository repository;
    private final PasswordEncoder encoder;

    public List<AdminDTO> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public AdminDTO findById(Long id) {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin " + id + " not found")));
    }

    public AdminDTO create(AdminDTO dto) {
        Admin saved = repository.save(
            Admin.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(encoder.encode(requirePwd(dto.getPassword()))) // encoder @RequiredArgsConstructor
                .role(Role.ADMIN)
                .build());
        return toDto(saved);
    }


    public AdminDTO update(Long id, AdminDTO dto) {
        Admin admin = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin " + id + " not found"));

        admin.setFullName(dto.getFullName());
        admin.setEmail(dto.getEmail());
        return toDto(repository.save(admin));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new NotFoundException("Admin " + id + " not found");
        repository.deleteById(id);
    }

    /* ---------- mapping helpers ---------- */
    private AdminDTO toDto(Admin a) {
        return AdminDTO.builder()
                .id(a.getId())
                .fullName(a.getFullName())
                .email(a.getEmail())
                .build();
    }

    private String requirePwd(String raw) {
        if (raw == null || raw.isBlank())
            throw new IllegalArgumentException("password is required");
        return raw;
    }
}
