package com.avtech.recruitment.domain.recruitment;

import com.avtech.recruitment.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    @Enumerated(EnumType.STRING)
    private JobType type; // FULL_TIME, PART_TIME, REMOTE

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    @Enumerated(EnumType.STRING)
    private JobStatus status; // DRAFT, OPEN, CLOSED

    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String currency; // USD, VND

    private Instant expiresAt;

    // AI Readiness: We will store keywords or vector embeddings later
    // @Column(columnDefinition = "json")
    // private String aiKeywords; 

    public enum JobType {
        FULL_TIME, PART_TIME, CONTRACT, FREELANCE, INTERNSHIP
    }

    public enum JobStatus {
        DRAFT, OPEN, CLOSED, ARCHIVED
    }
}
