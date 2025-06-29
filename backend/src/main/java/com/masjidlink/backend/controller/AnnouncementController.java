package com.masjidlink.backend.controller;

import com.masjidlink.backend.model.Announcement;
import com.masjidlink.backend.model.Mosque;
import com.masjidlink.backend.repository.AnnouncementRepository;
import com.masjidlink.backend.repository.MosqueRepository;
import com.masjidlink.backend.dto.AnnouncementResponse;
import com.masjidlink.backend.dto.AnnouncementRequest; // Import the new DTO
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private AnnouncementResponse convertToDto(Announcement announcement) {
        Mosque mosque = announcement.getMosque();
        return new AnnouncementResponse(
                announcement.getId(),
                announcement.getTitle(),
                announcement.getContent(),
                announcement.getPublishDate(),
                announcement.getExpirationDate(),
                announcement.isPublished(),
                announcement.getCreatedAt(),
                announcement.getUpdatedAt(),
                mosque.getId(),
                mosque.getName(),
                mosque.getLogoUrl()
        );
    }

    @GetMapping("/feed")
    public ResponseEntity<List<AnnouncementResponse>> getGlobalAnnouncementsFeed() {
        List<Announcement> announcements = announcementRepository.findActiveGlobalAnnouncements(
                LocalDateTime.now(), LocalDateTime.now()
        );
        List<AnnouncementResponse> dtos = announcements.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/mosque/{mosqueId}")
    public ResponseEntity<List<AnnouncementResponse>> getAnnouncementsByMosque(@PathVariable Long mosqueId) {
        Optional<Mosque> mosqueOptional = mosqueRepository.findById(mosqueId);
        if (mosqueOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Announcement> announcements = announcementRepository.findByMosqueOrderByPublishDateDesc(mosqueOptional.get());
        List<AnnouncementResponse> dtos = announcements.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getAnnouncementById(@PathVariable Long id) {
        Optional<Announcement> announcement = announcementRepository.findById(id);
        return announcement.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST: Create a new announcement
    @PostMapping
    public ResponseEntity<Announcement> createAnnouncement(@RequestBody AnnouncementRequest announcementRequest) { // <--- Changed input type
        // Manually load the Mosque entity based on the ID from the request DTO
        Optional<Mosque> mosqueOptional = mosqueRepository.findById(announcementRequest.getMosqueId());
        if (mosqueOptional.isEmpty()) {
            // Return a more informative error for the client
            return ResponseEntity.badRequest().body(null); // Or new ResponseEntity<>("Mosque not found with provided ID", HttpStatus.BAD_REQUEST);
        }
        Mosque mosque = mosqueOptional.get();

        // Create the Announcement entity from the DTO and the loaded Mosque
        Announcement announcement = new Announcement();
        announcement.setTitle(announcementRequest.getTitle());
        announcement.setContent(announcementRequest.getContent());
        announcement.setMosque(mosque); // Set the fully managed Mosque entity
        announcement.setPublishDate(LocalDateTime.now()); // Set publish date on backend
        announcement.setExpirationDate(
                announcementRequest.getExpirationDate() != null ? announcementRequest.getExpirationDate().atStartOfDay() : null
        ); // Convert LocalDate from DTO to LocalDateTime for entity
        announcement.setPublished(true);

        Announcement savedAnnouncement = announcementRepository.save(announcement);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAnnouncement);
    }

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
        // If you want to allow changing the mosque, you'd need similar logic here
        // to load the new mosque by ID and set it.

        Announcement updatedAnnouncement = announcementRepository.save(existingAnnouncement);
        return ResponseEntity.ok(updatedAnnouncement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long id) {
        if (!announcementRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        announcementRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}