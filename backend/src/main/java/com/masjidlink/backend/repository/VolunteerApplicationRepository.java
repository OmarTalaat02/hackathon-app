package com.masjidlink.backend.repository;

import com.masjidlink.backend.model.Mosque;
import com.masjidlink.backend.model.User;
import com.masjidlink.backend.model.VolunteerApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerApplicationRepository extends JpaRepository<VolunteerApplication, Long> {
    // Find all applications submitted by a specific user
    List<VolunteerApplication> findByUser(User user);

    // Find all applications for a specific mosque
    List<VolunteerApplication> findByMosqueOrderByApplicationDateDesc(Mosque mosque);

    // Find a specific application by user, mosque, and status
    Optional<VolunteerApplication> findByUserAndMosqueAndStatus(User user, Mosque mosque, String status); // Using String for enum for now
}