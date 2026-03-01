package com.avtech.recruitment.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TenantOnboardingDto {
    @NotBlank
    private String companyName;

    @NotBlank
    @Email
    private String adminEmail;

    @NotBlank
    private String password;
    
    private String domain;
}
