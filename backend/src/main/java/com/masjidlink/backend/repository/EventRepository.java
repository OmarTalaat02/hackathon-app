package com.masjidlink.backend.repository;

import com.masjidlink.backend.model.Event;
import com.masjidlink.backend.model.Mosque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Find all events for a specific mosque, ordered by event date
    List<Event> findByMosqueOrderByEventDateAscStartTimeAsc(Mosque mosque);

    // Find events by mosque and date (e.g., for daily schedules)
    List<Event> findByMosqueAndEventDateOrderByStartTimeAsc(Mosque mosque, LocalDate eventDate);

    // Find upcoming events for a mosque (event date after or equal to today)
    List<Event> findByMosqueAndEventDateGreaterThanEqualOrderByEventDateAscStartTimeAsc(Mosque mosque, LocalDate eventDate);
}