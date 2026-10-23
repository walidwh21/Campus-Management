package com.example.campus.event.service;

import com.example.campus.common.exception.NotFoundException;
import com.example.campus.event.dto.EventDTO;
import com.example.campus.event.model.Event;
import com.example.campus.event.repository.EventRepository;
import com.example.campus.schoolclass.repository.SchoolClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository repository;
    private final SchoolClassRepository classRepo;

    public List<EventDTO> findAll() { return repository.findAll().stream().map(this::toDto).toList(); }

    public EventDTO findById(Long id) {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event " + id + " not found")));
    }

    public EventDTO create(EventDTO dto) { return toDto(repository.save(toEntity(dto))); }

    public EventDTO update(Long id, EventDTO dto) {
        Event e = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event " + id + " not found"));
        e.setTitle(dto.getTitle());
        e.setDescription(dto.getDescription());
        e.setStartsAt(dto.getStartsAt());
        e.setEndsAt(dto.getEndsAt());
        e.setSchoolClass(resolveClass(dto.getClassId()));
        return toDto(repository.save(e));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new NotFoundException("Event " + id + " not found");
        repository.deleteById(id);
    }

    /* ---------- helpers ---------- */
    private EventDTO toDto(Event e) {
        return EventDTO.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .startsAt(e.getStartsAt())
                .endsAt(e.getEndsAt())
                .classId(e.getSchoolClass() == null ? null : e.getSchoolClass().getId())
                .build();
    }

    private Event toEntity(EventDTO d) {
        return Event.builder()
                .title(d.getTitle())
                .description(d.getDescription())
                .startsAt(d.getStartsAt())
                .endsAt(d.getEndsAt())
                .schoolClass(resolveClass(d.getClassId()))
                .build();
    }

    private com.example.campus.schoolclass.model.SchoolClass resolveClass(Long id) {
        if (id == null) return null;
        return classRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Class " + id + " not found"));
    }
}
