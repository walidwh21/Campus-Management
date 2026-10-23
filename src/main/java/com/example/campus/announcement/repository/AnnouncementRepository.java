package com.example.campus.announcement.repository;

import com.example.campus.announcement.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> { }
