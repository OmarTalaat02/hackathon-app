package com.masjidlink.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime publishDate;
    private LocalDateTime expirationDate;
    private boolean isPublished;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Include details of the associated Mosque directly
    private Long mosqueId;
    private String mosqueName;
    private String mosqueLogoUrl; // Optional, if needed directly in feed item
}