package com.masjidlink.backend.model;

import jakarta.persistence.*; // Use jakarta.persistence for Spring Boot 3+
import lombok.Data; // For getters, setters, etc. from Lombok
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity // Marks this class as a JPA entity, mapped to a database table
@Table(name = "users") // Explicitly define the table name as 'users' (good practice to avoid conflicts with 'user' keyword)
@Data //  to generate getters, setters, toString, equals, hashCode
@NoArgsConstructor //  to generate a no-argument constructor (required by JPA)
public class User {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID for PostgreSQL
    private Long id;

    @Column(unique = true, nullable = false) // Ensures username is unique and not null
    private String username;

    @Column(unique = true, nullable = false) // Ensures email is unique and not null
    private String email;

    @Column(nullable = false) // Password hash cannot be null
    private String passwordHash; // Store hashed password

    @Enumerated(EnumType.STRING) // Store enum as String in DB (ADMIN, CONGREGANT)
    @Column(nullable = false)
    private UserRole role; // Custom enum for roles

    private String firstName;
    private String lastName;
    private String phoneNumber;

    private LocalDate dateOfBirth; // For age check (e.g., for voting eligibility)

    @CreationTimestamp // Automatically sets creation timestamp
    @Column(nullable = false, updatable = false) // Cannot be null and not updatable after creation
    private LocalDateTime createdAt;

    @UpdateTimestamp // Automatically updates timestamp on entity modification
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Constructor for quick creation (optional, Lombok's @NoArgsConstructor covers default)
    public User(String username, String email, String passwordHash, UserRole role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}