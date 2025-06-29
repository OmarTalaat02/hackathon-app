package com.masjidlink.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate; // Use LocalDate for input date
// No LocalDateTime needed for simple input, unless frontend sends ISO string

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementRequest {
    private String title;
    private String content;
    private Long mosqueId; // Directly receive mosque ID
    private LocalDate expirationDate; // Receive as LocalDate
    private boolean isPublished;
}