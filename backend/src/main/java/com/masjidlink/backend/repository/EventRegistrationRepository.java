package com.masjidlink.backend.repository;

import com.masjidlink.backend.model.Event;
import com.masjidlink.backend.model.EventRegistration;
import com.masjidlink.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    // Find a specific registration by user and event
    Optional<EventRegistration> findByUserAndEvent(User user, Event event);

    // Find all registrations for a specific user
    List<EventRegistration> findByUser(User user);

    // Find all registrations for a specific event
    List<EventRegistration> findByEvent(Event event);

    // Check if a user has already registered for a specific event
    boolean existsByUserAndEvent(User user, Event event);
}