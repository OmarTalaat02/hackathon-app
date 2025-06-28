package com.masjidlink.backend.repository;

import com.masjidlink.backend.model.Membership;
import com.masjidlink.backend.model.Mosque;
import com.masjidlink.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    // Find a specific membership by user and mosque
    Optional<Membership> findByUserAndMosque(User user, Mosque mosque);

    // Find all memberships for a user
    List<Membership> findByUser(User user);

    // Find all members of a specific mosque
    List<Membership> findByMosque(Mosque mosque);

    // Check if a user has an active membership for a specific mosque
    boolean existsByUserAndMosqueAndIsActiveTrue(User user, Mosque mosque);
}