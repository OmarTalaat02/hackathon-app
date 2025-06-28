package com.masjidlink.backend.repository;

import com.masjidlink.backend.model.Mosque;
import com.masjidlink.backend.model.PrayerTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Repository
public interface PrayerTimeRepository extends JpaRepository<PrayerTime, Long> {
    // Find prayer times for a specific mosque on a specific date
    Optional<PrayerTime> findByMosqueAndDate(Mosque mosque, LocalDate date);

    // Find all prayer times for a mosque (e.g., for a calendar view)
    List<PrayerTime> findByMosqueOrderByDateAsc(Mosque mosque);
}