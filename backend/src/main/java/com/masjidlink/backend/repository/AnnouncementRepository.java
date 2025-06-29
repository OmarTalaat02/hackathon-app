package com.masjidlink.backend.repository;

import com.masjidlink.backend.model.Announcement;
import com.masjidlink.backend.model.Mosque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Import this
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    // Find all announcements for a specific mosque, ordered by publish date
    List<Announcement> findByMosqueOrderByPublishDateDesc(Mosque mosque);

    // --- MODIFIED METHOD FOR GLOBAL ANNOUNCEMENTS FEED ---
    // Find all published announcements that are currently active (publish_date <= now AND (expiration_date >= now OR expiration_date IS NULL))
    @Query("SELECT a FROM Announcement a WHERE a.isPublished = TRUE AND a.publishDate <= ?1 AND (a.expirationDate >= ?2 OR a.expirationDate IS NULL) ORDER BY a.publishDate DESC")
    List<Announcement> findActiveGlobalAnnouncements(LocalDateTime nowPublish, LocalDateTime nowExpire);
}