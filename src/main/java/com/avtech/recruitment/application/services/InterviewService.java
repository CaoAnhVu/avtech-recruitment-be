package com.avtech.recruitment.application.services;

import com.avtech.recruitment.application.dto.interview.CreateInterviewRequest;
import com.avtech.recruitment.application.events.InterviewScheduledEvent;
import com.avtech.recruitment.domain.recruitment.Application;
import com.avtech.recruitment.domain.recruitment.Interview;
import com.avtech.recruitment.infrastructure.persistence.ApplicationRepository;
import com.avtech.recruitment.infrastructure.persistence.InterviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Interview scheduleInterview(CreateInterviewRequest request) {
        log.info("Scheduling interview for application: {}", request.getApplicationId());

        if (request.getStartTime().isAfter(request.getEndTime()) || request.getStartTime().equals(request.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        // Overlap Detection: Enforce that no interviewerId has 2 overlapping interviews within the same tenant_id.
        // The tenant filter handles isolation, we just need to check the interviewer.
        long overlappingCount = interviewRepository.countOverlappingInterviews(
                request.getInterviewerId(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (overlappingCount > 0) {
            throw new IllegalArgumentException("Interviewer has overlapping interviews at this time");
        }

        Interview interview = Interview.builder()
                .application(application)
                .interviewerId(request.getInterviewerId())
                .startTime(request.getStartTime()) // Stored as UTC ISO-8601 inherently due to Instant
                .endTime(request.getEndTime())
                .locationType(request.getLocationType())
                .meetingLink(request.getMeetingLink())
                .status(Interview.InterviewStatus.SCHEDULED)
                .build();

        Interview savedInterview = interviewRepository.save(interview);

        // Emit Event for Notifications (Email + ICS generated async)
        eventPublisher.publishEvent(new InterviewScheduledEvent(this, savedInterview));

        return savedInterview;
    }

    @Transactional(readOnly = true)
    public List<Interview> getInterviewsByDateRange(Instant from, Instant to) {
        // Because of the tenant filter, this will only return interviews for the current tenant.
        // We can fetch all and filter in memory, or write a custom query.
        // For simplicity right now, fetch all and filter, or we will add a query to repository.
        // Let's assume we want a simple JPA findAll for now. 
        // A better approach is calling a repository method findByStartTimeBetween(from, to).
        // For MVP, we'll fetch all and filter (since tenant filter limits it heavily anyway), 
        // but let's implement the DB level filter if required.
        
        return interviewRepository.findAll().stream()
                .filter(i -> !i.getStartTime().isBefore(from) && !i.getStartTime().isAfter(to))
                .toList();
    }
}
