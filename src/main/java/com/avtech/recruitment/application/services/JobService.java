package com.avtech.recruitment.application.services;

import com.avtech.recruitment.application.dto.job.CreateJobRequest;
import com.avtech.recruitment.domain.recruitment.Job;
import com.avtech.recruitment.infrastructure.persistence.JobRepository;
import com.avtech.recruitment.infrastructure.tenant.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Transactional
    public Job createJob(CreateJobRequest request) {
        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .requirements(request.getRequirements())
                .location(request.getLocation())
                .type(request.getType())
                .status(Job.JobStatus.DRAFT) // Default to DRAFT
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .currency(request.getCurrency())
                .expiresAt(request.getExpiresAt())
                .build();
        
        // TenantID is automatically set by BaseEntity @PrePersist using TenantContext
        return jobRepository.save(job);
    }

    public Page<Job> getJobsByTenant(Pageable pageable) {
        String tenantId = TenantContext.getTenantId();
        // Securely fetch only records governed by the contextual User's Token
        return jobRepository.findAll((root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("tenantId"), tenantId), pageable);
    }

    public Job getJobById(String id) {
       Job job = jobRepository.findById(id)
               .orElseThrow(() -> new IllegalArgumentException("Job not found: " + id));

       if (!job.getTenantId().equals(TenantContext.getTenantId())) {
           throw new SecurityException("Unauthorized access to Job");
       }
       return job;
    }

    @Transactional
    public Job updateJob(String id, com.avtech.recruitment.application.dto.job.UpdateJobRequest request) {
        Job existingJob = getJobById(id); // Already validates ownership

        if (request.getTitle() != null) existingJob.setTitle(request.getTitle());
        if (request.getDescription() != null) existingJob.setDescription(request.getDescription());
        if (request.getRequirements() != null) existingJob.setRequirements(request.getRequirements());
        if (request.getLocation() != null) existingJob.setLocation(request.getLocation());
        if (request.getType() != null) existingJob.setType(com.avtech.recruitment.domain.recruitment.Job.JobType.valueOf(request.getType()));
        if (request.getStatus() != null) existingJob.setStatus(request.getStatus());
        if (request.getSalaryMin() != null) existingJob.setSalaryMin(request.getSalaryMin());
        if (request.getSalaryMax() != null) existingJob.setSalaryMax(request.getSalaryMax());
        if (request.getCurrency() != null) existingJob.setCurrency(request.getCurrency());
        if (request.getExpiresAt() != null) existingJob.setExpiresAt(request.getExpiresAt());

        return jobRepository.save(existingJob);
    }

    @Transactional
    public void deleteJob(String id) {
        Job existingJob = getJobById(id); // Validates ownership
        jobRepository.delete(existingJob);
    }
}
