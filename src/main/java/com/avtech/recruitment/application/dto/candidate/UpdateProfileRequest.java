package com.avtech.recruitment.application.dto.candidate;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @NotBlank
    private String fullName;
    
    private String headline;
    private String summary;
    private String skills;
    private String phoneNumber;
    private String cvUrl;
    private String linkedInUrl;
    private String portfolioUrl;
}
