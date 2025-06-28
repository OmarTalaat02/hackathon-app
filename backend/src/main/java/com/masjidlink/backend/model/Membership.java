package com.masjidlink.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal; // For currency values
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "memberships", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "mosque_id"}) // User can have only one membership per mosque
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mosque_id", nullable = false)
    private Mosque mosque;

    @Column(name = "membership_type", nullable = false)
    private String membershipType; // e.g., "Annual", "Founding Member"

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date") // Can be null for lifetime, or populated for annual
    private LocalDate endDate;

    @Column(name = "initial_fee_amount", nullable = false, precision = 10, scale = 2) // Total 10 digits, 2 after decimal
    private BigDecimal initialFeeAmount;

    @Column(name = "annual_fee_amount", precision = 10, scale = 2) // Can be null, same precision/scale
    private BigDecimal annualFeeAmount;

    @Column(name = "is_initial_fee_paid", nullable = false)
    private boolean isInitialFeePaid = false;

    @Column(name = "is_annual_fee_paid", nullable = false)
    private boolean isAnnualFeePaid = false;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true; // Based on dates and payment status (conceptually)

    @Column(name = "can_vote", nullable = false)
    private boolean canVote = false; // Admin-set / derived conceptually

    @Column(name = "is_board_member", nullable = false)
    private boolean isBoardMember = false; // Admin-set

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}