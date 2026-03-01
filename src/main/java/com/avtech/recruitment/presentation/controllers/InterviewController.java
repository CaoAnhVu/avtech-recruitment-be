package com.avtech.recruitment.presentation.controllers;

import com.avtech.recruitment.application.dto.interview.CreateInterviewRequest;
import com.avtech.recruitment.application.services.InterviewService;
import com.avtech.recruitment.domain.recruitment.Interview;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/interviews")
@RequiredArgsConstructor
@Tag(name = "Interview Management", description = "APIs for scheduling and viewing interviews")
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    @PreAuthorize("hasAnyRole('HR', 'EMPLOYER')")
    @Operation(summary = "Schedule a new Interview", description = "Schedules an interview and prevents overlapping logic")
    public ResponseEntity<?> scheduleInterview(@Valid @RequestBody CreateInterviewRequest request) {
        try {
            Interview interview = interviewService.scheduleInterview(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(interview);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('HR', 'EMPLOYER')")
    @Operation(summary = "Get interviews by date range", description = "Fetch interviews within a specific time segment")
    public ResponseEntity<List<Interview>> getInterviews(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        
        List<Interview> interviews = interviewService.getInterviewsByDateRange(from, to);
        return ResponseEntity.ok(interviews);
    }
}
