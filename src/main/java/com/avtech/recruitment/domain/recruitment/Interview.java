package com.avtech.recruitment.domain.recruitment;

import com.avtech.recruitment.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "interviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "interviewer_id", nullable = false)
    private String interviewerId;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private LocationType locationType;

    @Column(name = "meeting_link")
    private String meetingLink;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private InterviewStatus status;

    public enum LocationType {
        ONLINE,
        OFFLINE
    }

    public enum InterviewStatus {
        SCHEDULED,
        CANCELLED,
        COMPLETED
    }
}
