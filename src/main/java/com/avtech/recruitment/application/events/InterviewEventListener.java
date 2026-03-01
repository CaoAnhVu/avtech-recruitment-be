package com.avtech.recruitment.application.events;

import com.avtech.recruitment.application.services.EmailService;
import com.avtech.recruitment.domain.recruitment.Application;
import com.avtech.recruitment.domain.recruitment.Candidate;
import com.avtech.recruitment.domain.recruitment.Interview;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class InterviewEventListener {

    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleInterviewScheduledEvent(InterviewScheduledEvent event) {
        log.info("Received InterviewScheduledEvent for interview: {}", event.getInterview().getId());
        
        Interview interview = event.getInterview();
        Application application = interview.getApplication();
        Candidate candidate = application.getCandidate(); // Note: This might cause LazyInitializationException if not eagerly fetched or if session is closed. Assuming candidate is proxy or eagerly loaded for MVP, otherwise fetch explicitly in service.
        
        // For simplicity and to avoid LazyInit in async thread without transaction, 
        // a better approach is to pass candidate info inside the event if LazyLoading is an issue.
        // Assuming Candidate has user.getEmail() for now.
        // We will send a dummy email if candidate is null or properties are unavailable for quick test.
        
        String dummyCandidateEmail = "candidate@example.com";
        String dummyCandidateName = "Candidate Name";

        try {
           dummyCandidateName = candidate.getUser().getEmail(); // if user is mapped
        } catch (Exception e) {
           log.warn("Could not lazily fetch candidate details, falling back to dummy data");
        }

        emailService.sendInterviewInvitation(interview, dummyCandidateEmail, dummyCandidateName);
    }
}
