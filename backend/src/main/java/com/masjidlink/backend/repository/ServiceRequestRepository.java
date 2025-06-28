package com.masjidlink.backend.repository;

import com.masjidlink.backend.model.Mosque;
import com.masjidlink.backend.model.ServiceRequest;
import com.masjidlink.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    // Find all service requests by a specific user
    List<ServiceRequest> findByUserOrderByCreatedAtDesc(User user);

    // Find all service requests for a specific mosque
    List<ServiceRequest> findByMosqueOrderByCreatedAtDesc(Mosque mosque);
}