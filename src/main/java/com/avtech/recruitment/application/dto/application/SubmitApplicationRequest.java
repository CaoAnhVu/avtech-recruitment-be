package com.avtech.recruitment.application.dto.application;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubmitApplicationRequest {
    @NotBlank(message = "Job ID is required")
    private String jobId;
    
    private String coverLetter;
}
