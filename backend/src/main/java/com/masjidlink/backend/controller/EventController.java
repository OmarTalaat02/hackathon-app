package com.masjidlink.backend.controller;

import com.masjidlink.backend.model.Event;
import com.masjidlink.backend.model.Mosque;
import com.masjidlink.backend.model.EventStatus;
import com.masjidlink.backend.repository.EventRepository;
import com.masjidlink.backend.repository.MosqueRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime; // Ensure LocalTime is imported for event times
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventRepository eventRepository;
    private final MosqueRepository mosqueRepository;

    public EventController(EventRepository eventRepository,
                           MosqueRepository mosqueRepository) {
        this.eventRepository = eventRepository;
        this.mosqueRepository = mosqueRepository;
    }

    // GET all upcoming events globally
    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        List<Event> events = eventRepository.findUpcomingEvents(LocalDate.now());
        return ResponseEntity.ok(events);
    }

    // GET all events for a specific mosque
    @GetMapping("/mosque/{mosqueId}")
    public ResponseEntity<List<Event>> getEventsByMosque(@PathVariable Long mosqueId) {
        Optional<Mosque> mosqueOptional = mosqueRepository.findById(mosqueId);
        if (mosqueOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Event> events = eventRepository.findByMosqueOrderByEventDateAscStartTimeAsc(mosqueOptional.get());
        return ResponseEntity.ok(events);
    }

    // GET a specific event by ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventRepository.findById(id);
        return event.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST: Create a new event
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Optional<Mosque> mosqueOptional = mosqueRepository.findById(event.getMosque().getId());
        if (mosqueOptional.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Mosque not found
        }

        event.setMosque(mosqueOptional.get());
        event.setStatus(EventStatus.SCHEDULED); // Default status for new events
        event.setCreatedAt(LocalDateTime.now()); // Set creation timestamp

        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    // PUT: Update an existing event
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        Optional<Event> existingEventOptional = eventRepository.findById(id);
        if (existingEventOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event existingEvent = existingEventOptional.get();
        existingEvent.setTitle(eventDetails.getTitle());
        existingEvent.setDescription(eventDetails.getDescription());
        existingEvent.setEventDate(eventDetails.getEventDate());
        existingEvent.setStartTime(eventDetails.getStartTime());
        existingEvent.setEndTime(eventDetails.getEndTime());
        existingEvent.setLocation(eventDetails.getLocation());
        existingEvent.setCapacity(eventDetails.getCapacity());
        existingEvent.setRegistrationRequired(eventDetails.isRegistrationRequired());
        existingEvent.setStatus(eventDetails.getStatus());
        // Do not update Mosque from here to maintain data integrity

        Event updatedEvent = eventRepository.save(existingEvent);
        return ResponseEntity.ok(updatedEvent);
    }

    // DELETE: Delete an event
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}