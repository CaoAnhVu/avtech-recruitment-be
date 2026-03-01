package com.avtech.recruitment.presentation.controllers;

import com.avtech.recruitment.application.dto.application.SubmitApplicationRequest;
import com.avtech.recruitment.application.dto.candidate.UpdateProfileRequest;
import com.avtech.recruitment.application.services.ApplicationService;
import com.avtech.recruitment.domain.recruitment.Application;
import com.avtech.recruitment.domain.recruitment.Candidate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.avtech.recruitment.application.dto.ats.UpdateApplicationStageRequest;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@Tag(name = "Candidate & Application", description = "APIs for Candidates to manage profile and apply")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/profile")
    @Operation(summary = "Update Candidate Profile", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Candidate> updateProfile(@RequestBody UpdateProfileRequest request) {
        // Map DTO to Entity (Simplified)
        Candidate candidate = Candidate.builder()
                .fullName(request.getFullName())
                .headline(request.getHeadline())
                .summary(request.getSummary())
                .skills(request.getSkills())
                .phoneNumber(request.getPhoneNumber())
                .build();
        
        return ResponseEntity.ok(applicationService.createOrUpdateProfile(candidate));
    }

    @PostMapping("/apply")
    @Operation(summary = "Apply for a Job", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Application> applyForJob(@Valid @RequestBody SubmitApplicationRequest request) {
        return ResponseEntity.ok(applicationService.applyForJob(request.getJobId(), request.getCoverLetter()));
    }

    @GetMapping("/job/{jobId}")
    @Operation(summary = "List Applications under a Job ID", description = "Retrieves all Candidate profiles applied to a Job for Kanban column sorting.", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Application>> getApplicationsByJob(@PathVariable String jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsByJob(jobId));
    }

    @PatchMapping("/{id}/stage")
    @Operation(summary = "Migrate Candidate Stage", description = "Patch the Application status enum to transition Candidate from one Kanban column to another.", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Application> updateApplicationStage(@PathVariable String id, @Valid @RequestBody UpdateApplicationStageRequest request) {
        return ResponseEntity.ok(applicationService.updateApplicationStage(id, request));
    }
}
