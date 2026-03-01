package com.avtech.recruitment.domain.recruitment;

import com.avtech.recruitment.domain.common.BaseEntity;
import com.avtech.recruitment.domain.identity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "candidates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // Link to Identity User (One-to-One)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private String fullName;

    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String headline; // e.g. "Senior Java Developer"

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String skills; // JSON array or comma separated

    private String cvUrl; // Link to stored CV file

    private String linkedInUrl;
    private String portfolioUrl;

    // AI Scoring Readiness
    // @Column(columnDefinition = "vector")
    // private float[] embedding;
}
