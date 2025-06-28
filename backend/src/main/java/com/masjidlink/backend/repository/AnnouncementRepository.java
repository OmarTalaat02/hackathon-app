package com.masjidlink.backend.repository;

import com.masjidlink.backend.model.Announcement;
import com.masjidlink.backend.model.Mosque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    // Find all announcements for a specific mosque, ordered by publish date
    List<Announcement> findByMosqueOrderByPublishDateDesc(Mosque mosque);

    // Find all published announcements for a specific mosque, not expired, ordered by publish date
    List<Announcement> findByMosqueAndIsPublishedTrueAndExpirationDateGreaterThanOrderByPublishDateDesc(Mosque mosque, LocalDateTime now);

    // Find all published announcements from ALL mosques, not expired, ordered by publish date (for the global feed)
    List<Announcement> findByIsPublishedTrueAndExpirationDateGreaterThanOrderByPublishDateDesc(LocalDateTime now);

    // Find all published announcements from ALL mosques that are currently active (published and not expired or no expiration date)
    List<Announcement> findByIsPublishedTrueAndPublishDateLessThanEqualAndExpirationDateGreaterThanEqualOrExpirationDateIsNullOrderByPublishDateDesc(
            LocalDateTime nowPublish, LocalDateTime nowExpire
    );
}