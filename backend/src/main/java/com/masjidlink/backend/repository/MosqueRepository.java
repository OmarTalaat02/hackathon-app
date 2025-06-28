package com.masjidlink.backend.repository;

import com.masjidlink.backend.model.Mosque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MosqueRepository extends JpaRepository<Mosque, Long> {
    Optional<Mosque> findByName(String name);
    boolean existsByName(String name);
}