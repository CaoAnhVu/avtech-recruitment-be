package com.avtech.recruitment.application.dto.interview;

import com.avtech.recruitment.domain.recruitment.Interview;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class CreateInterviewRequest {

    @NotBlank(message = "Application ID is required")
    private String applicationId;

    @NotBlank(message = "Interviewer ID is required")
    private String interviewerId;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private Instant startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private Instant endTime;

    @NotNull(message = "Location type is required")
    private Interview.LocationType locationType;

    private String meetingLink;
}
