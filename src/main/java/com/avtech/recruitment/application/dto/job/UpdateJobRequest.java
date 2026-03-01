package com.avtech.recruitment.application.dto.job;

import lombok.Data;
import com.avtech.recruitment.domain.recruitment.Job.JobStatus;

import java.time.Instant;
import java.math.BigDecimal;

@Data
public class UpdateJobRequest {
    private String title;
    private String description;
    private String requirements;
    private String location;
    private String type;
    private JobStatus status;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String currency;
    private Instant expiresAt;
}
