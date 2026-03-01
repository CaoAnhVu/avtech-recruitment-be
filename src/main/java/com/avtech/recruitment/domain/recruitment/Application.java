package com.avtech.recruitment.domain.recruitment;

import com.avtech.recruitment.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private ApplicationStatus status;

    @Column(name = "ai_match_score")
    private Double aiMatchScore; // 0.0 to 1.0 (or 100.0)

    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    public enum ApplicationStatus {
        APPLIED,
        SCREENING,
        INTERVIEW_SCHEDULED,
        OFFER_MADE,
        HIRED,
        REJECTED
    }
}
