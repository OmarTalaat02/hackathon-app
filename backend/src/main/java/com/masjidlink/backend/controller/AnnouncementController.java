package com.masjidlink.backend.controller;

import com.masjidlink.backend.model.Announcement;
import com.masjidlink.backend.model.Mosque;
import com.masjidlink.backend.repository.AnnouncementRepository;
import com.masjidlink.backend.repository.MosqueRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/announcements")
public class AnnouncementController {

    private final AnnouncementRepository announcementRepository;
    private final MosqueRepository mosqueRepository;

    public AnnouncementController(AnnouncementRepository announcementRepository,
                                  MosqueRepository mosqueRepository) {
        this.announcementRepository = announcementRepository;
        this.mosqueRepository = mosqueRepository;
    }

    // GET all active announcements for the global feed
    @GetMapping("/feed")
    public ResponseEntity<List<Announcement>> getGlobalAnnouncementsFeed() {
        List<Announcement> announcements = announcementRepository.findByIsPublishedTrueAndPublishDateLessThanEqualAndExpirationDateGreaterThanEqualOrExpirationDateIsNullOrderByPublishDateDesc(
                LocalDateTime.now(), LocalDateTime.now()
        );
        return ResponseEntity.ok(announcements);
    }

    // GET announcements for a specific mosque
    @GetMapping("/mosque/{mosqueId}")
    public ResponseEntity<List<Announcement>> getAnnouncementsByMosque(@PathVariable Long mosqueId) {
        Optional<Mosque> mosqueOptional = mosqueRepository.findById(mosqueId);
        if (mosqueOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Announcement> announcements = announcementRepository.findByMosqueOrderByPublishDateDesc(mosqueOptional.get());
        return ResponseEntity.ok(announcements);
    }

    // GET a specific announcement by ID
    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getAnnouncementById(@PathVariable Long id) {
        Optional<Announcement> announcement = announcementRepository.findById(id);
        return announcement.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST: Create a new announcement
    @PostMapping
    public ResponseEntity<Announcement> createAnnouncement(@RequestBody Announcement announcement) {
        Optional<Mosque> mosqueOptional = mosqueRepository.findById(announcement.getMosque().getId());
        if (mosqueOptional.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Mosque not found
        }

        announcement.setMosque(mosqueOptional.get());
        announcement.setPublishDate(LocalDateTime.now());
        announcement.setPublished(true); // Default to true on creation

        Announcement savedAnnouncement = announcementRepository.save(announcement);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAnnouncement);
    }

    // PUT: Update an existing announcement
    @PutMapping("/{id}")
    public ResponseEntity<Announcement> updateAnnouncement(@PathVariable Long id, @RequestBody Announcement announcementDetails) {
        Optional<Announcement> existingAnnouncementOptional = announcementRepository.findById(id);
        if (existingAnnouncementOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Announcement existingAnnouncement = existingAnnouncementOptional.get();
        existingAnnouncement.setTitle(announcementDetails.getTitle());
        existingAnnouncement.setContent(announcementDetails.getContent());
        existingAnnouncement.setExpirationDate(announcementDetails.getExpirationDate());
        existingAnnouncement.setPublished(announcementDetails.isPublished());
        // Do not update Mosque or createdByUser from here to maintain data integrity or because they are not present

        Announcement updatedAnnouncement = announcementRepository.save(existingAnnouncement);
        return ResponseEntity.ok(updatedAnnouncement);
    }

    // DELETE: Delete an announcement
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long id) {
        if (!announcementRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        announcementRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}