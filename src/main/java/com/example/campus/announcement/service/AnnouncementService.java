package com.example.campus.announcement.service;

import com.example.campus.common.exception.NotFoundException;
import com.example.campus.announcement.dto.AnnouncementDTO;
import com.example.campus.announcement.model.Announcement;
import com.example.campus.announcement.repository.AnnouncementRepository;
import com.example.campus.schoolclass.repository.SchoolClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository repository;
    private final SchoolClassRepository classRepo;

    public List<AnnouncementDTO> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public AnnouncementDTO findById(Long id) {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Announcement " + id + " not found")));
    }

    public AnnouncementDTO create(AnnouncementDTO dto) {
        if (dto.getPublishedAt() == null) dto.setPublishedAt(LocalDateTime.now());
        return toDto(repository.save(toEntity(dto)));
    }

    public AnnouncementDTO update(Long id, AnnouncementDTO dto) {
        Announcement a = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Announcement " + id + " not found"));
        a.setTitle(dto.getTitle());
        a.setContent(dto.getContent());
        a.setPublishedAt(dto.getPublishedAt());
        a.setSchoolClass(resolveClass(dto.getClassId()));
        return toDto(repository.save(a));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new NotFoundException("Announcement " + id + " not found");
        repository.deleteById(id);
    }

    /* ---------- helpers ---------- */
    private AnnouncementDTO toDto(Announcement a) {
        return AnnouncementDTO.builder()
                .id(a.getId())
                .title(a.getTitle())
                .content(a.getContent())
                .publishedAt(a.getPublishedAt())
                .classId(a.getSchoolClass() == null ? null : a.getSchoolClass().getId())
                .build();
    }

    private Announcement toEntity(AnnouncementDTO d) {
        return Announcement.builder()
                .title(d.getTitle())
                .content(d.getContent())
                .publishedAt(d.getPublishedAt())
                .schoolClass(resolveClass(d.getClassId()))
                .build();
    }

    private com.example.campus.schoolclass.model.SchoolClass resolveClass(Long id) {
        if (id == null) return null;
        return classRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Class " + id + " not found"));
    }
}
