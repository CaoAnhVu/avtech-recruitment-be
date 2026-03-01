package com.avtech.recruitment.application.dto.job;

import com.avtech.recruitment.domain.recruitment.Job;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CreateJobRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private String requirements;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Job Type is required")
    private Job.JobType type;

    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String currency = "USD";

    @Future(message = "Expiration date must be in the future")
    private Instant expiresAt;
}
