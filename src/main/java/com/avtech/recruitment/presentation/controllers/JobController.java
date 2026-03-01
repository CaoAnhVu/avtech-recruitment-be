package com.avtech.recruitment.presentation.controllers;

import com.avtech.recruitment.application.dto.job.CreateJobRequest;
import com.avtech.recruitment.application.services.JobService;
import com.avtech.recruitment.domain.recruitment.Job;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Job Management", description = "APIs for posting and searching jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    @Operation(summary = "Create a Job Post", description = "Requires HR Role. Creates a job under the current tenant context.", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Job> createJob(@Valid @RequestBody CreateJobRequest request) {
        Job job = jobService.createJob(request);
        return ResponseEntity.ok(job);
    }

    @GetMapping
    @Operation(summary = "List Jobs", description = "Get list of jobs with pagination. Currently scoped to tenant context if authenticated.")
    public ResponseEntity<Page<Job>> getJobs(Pageable pageable) {
        return ResponseEntity.ok(jobService.getJobsByTenant(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Job Details", description = "Get specific job by ID.")
    public ResponseEntity<Job> getJob(@PathVariable String id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing Job Post", description = "Requires HR or Employer Role. Modifies data representing an active job under current schema bounds.", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Job> updateJob(@PathVariable String id, @RequestBody com.avtech.recruitment.application.dto.job.UpdateJobRequest request) {
        return ResponseEntity.ok(jobService.updateJob(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an Active Job Post", description = "Purges the job profile natively from the RDS index utilizing local ownership verification.", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteJob(@PathVariable String id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
