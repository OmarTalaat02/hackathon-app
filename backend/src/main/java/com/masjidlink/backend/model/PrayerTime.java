package com.masjidlink.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "prayer_times", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"mosque_id", "date"}) // Only one entry per mosque per day
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrayerTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mosque_id", nullable = false)
    private Mosque mosque;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime fajr;

    @Column(nullable = false)
    private LocalTime dhuhr;

    @Column(nullable = false)
    private LocalTime asr;

    @Column(nullable = false)
    private LocalTime maghrib;

    @Column(nullable = false)
    private LocalTime isha;

    private LocalTime sunrise; // Can be null

    private LocalTime juma; // Can be null

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}