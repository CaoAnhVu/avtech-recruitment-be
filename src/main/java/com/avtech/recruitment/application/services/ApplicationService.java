package com.avtech.recruitment.application.services;

import com.avtech.recruitment.domain.identity.User;
import com.avtech.recruitment.domain.recruitment.Application;
import com.avtech.recruitment.domain.recruitment.Candidate;
import com.avtech.recruitment.domain.recruitment.Job;
import com.avtech.recruitment.infrastructure.persistence.ApplicationRepository;
import com.avtech.recruitment.infrastructure.persistence.CandidateRepository;
import com.avtech.recruitment.infrastructure.persistence.JobRepository;
import com.avtech.recruitment.infrastructure.persistence.UserRepository;
import com.avtech.recruitment.infrastructure.security.UserPrincipal;
import com.avtech.recruitment.application.dto.ats.UpdateApplicationStageRequest;
import com.avtech.recruitment.infrastructure.tenant.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Candidate createOrUpdateProfile(Candidate candidateData) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(principal.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        Candidate candidate = candidateRepository.findByUserId(user.getId())
                .orElse(new Candidate());

        if (candidate.getUser() == null) {
            candidate.setUser(user);
        }
        
        candidate.setFullName(candidateData.getFullName());
        candidate.setHeadline(candidateData.getHeadline());
        candidate.setSummary(candidateData.getSummary());
        candidate.setSkills(candidateData.getSkills());
        candidate.setPhoneNumber(candidateData.getPhoneNumber());
        // ... set other fields

        return candidateRepository.save(candidate);
    }

    @Transactional
    public Application applyForJob(String jobId, String coverLetter) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Candidate candidate = candidateRepository.findByUserId(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("Candidate profile not found. Please complete profile first."));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (applicationRepository.existsByJobIdAndCandidateId(jobId, candidate.getId())) {
             throw new IllegalArgumentException("You have already applied for this job.");
        }

        Application application = Application.builder()
                .job(job)
                .candidate(candidate)
                .coverLetter(coverLetter)
                .status(Application.ApplicationStatus.APPLIED)
                .build();
        
        // TenantID logic: Application should probably belong to the Job's Tenant?
        // Or it belongs to the Candidate?
        // Usually, Application is a record IN the Tenant's system.
        // So we should set tenantId = job.getTenantId().
        // If BaseEntity automatically sets from Context, we must ensure Context is correct.
        // BUT, the Candidate is likely in a "Public" or "Personal" context when applying?
        // NO, when applying, they are interacting with the Tenant's Job.
        // So we should ideally Switch Context to the Job's Tenant before saving.
        
        // However, for MVP with shared schema, we can manually set it if BaseEntity allows setter.
        // application.setTenantId(job.getTenantId()); // If setter is exposed.

        return applicationRepository.save(application);
    }

    public List<Application> getApplicationsByJob(String jobId) {
        Job job = jobRepository.findById(jobId)
                    .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (!job.getTenantId().equals(TenantContext.getTenantId())) {
             throw new SecurityException("Unauthorized access to Job Details");
        }

        // Fetch chronological
        return applicationRepository.findByJobIdOrderByCreatedAtDesc(jobId);
    }

    public Application getApplicationById(String applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application ID not found"));

        if (!app.getJob().getTenantId().equals(TenantContext.getTenantId())) {
            throw new SecurityException("Unauthorized access to Candidate Application");
        }
        return app;
    }

    @Transactional
    public Application updateApplicationStage(String applicationId, UpdateApplicationStageRequest request) {
        Application existing = getApplicationById(applicationId); 
        existing.setStatus(request.getStatus());
        return applicationRepository.save(existing);
    }
}
