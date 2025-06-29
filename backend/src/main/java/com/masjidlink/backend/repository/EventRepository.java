package com.masjidlink.backend.repository;

import com.masjidlink.backend.model.Event;
import com.masjidlink.backend.model.Mosque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Import Query
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Find all events for a specific mosque, ordered by event date
    List<Event> findByMosqueOrderByEventDateAscStartTimeAsc(Mosque mosque);

    // Find events by mosque and date (e.g., for daily schedules)
    List<Event> findByMosqueAndEventDateOrderByStartTimeAsc(Mosque mosque, LocalDate eventDate);

    // --- MODIFIED METHOD FOR UPCOMING EVENTS ---
    // Find upcoming events (event date on or after today's date), ordered by event date and start time
    @Query("SELECT e FROM Event e WHERE e.eventDate >= ?1 ORDER BY e.eventDate ASC, e.startTime ASC")
    List<Event> findUpcomingEvents(LocalDate currentDate);

    // (You can remove the old problematic method if it's still there)
    // List<Event> findByMosqueAndEventDateGreaterThanEqualOrderByEventDateAscStartTimeAsc(Mosque mosque, LocalDate eventDate);
}