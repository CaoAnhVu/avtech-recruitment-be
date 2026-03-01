package com.avtech.recruitment.application.dto.tenant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterTenantRequest {
    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Domain is required")
    private String domain;

    @NotBlank(message = "Admin email is required")
    @Email(message = "Invalid email format")
    private String adminEmail;

    @NotBlank(message = "Admin password is required")
    private String adminPassword;
    
    // Optional: Plan ID, if selected during registration
    private String planId;
}
