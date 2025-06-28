package com.masjidlink.backend.repository;

import com.masjidlink.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Marks this interface as a Spring Data JPA repository
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository provides methods like save(), findById(), findAll(), deleteById(), etc.

    // Custom method to find a user by username
    Optional<User> findByUsername(String username);

    // Custom method to find a user by email
    Optional<User> findByEmail(String email);

    // Custom method to check if a user with a given username exists
    boolean existsByUsername(String username);

    // Custom method to check if a user with a given email exists
    boolean existsByEmail(String email);
}