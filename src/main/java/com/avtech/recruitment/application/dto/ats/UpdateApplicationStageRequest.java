package com.avtech.recruitment.application.dto.ats;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import com.avtech.recruitment.domain.recruitment.Application.ApplicationStatus;

@Data
public class UpdateApplicationStageRequest {
    
    @NotNull(message = "Status cannot be null")
    private ApplicationStatus status;
}
