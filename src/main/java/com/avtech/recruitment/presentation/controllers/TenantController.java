package com.avtech.recruitment.presentation.controllers;

import com.avtech.recruitment.application.dto.tenant.RegisterTenantRequest;
import com.avtech.recruitment.application.services.TenantService;
import com.avtech.recruitment.domain.tenant.Tenant;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@RestController
@RequestMapping("/api/public/tenants")
@Tag(name = "Tenant Management", description = "APIs for registering and managing companies")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @PostMapping("/register")
    @Operation(summary = "Register a new Company/Tenant", description = "Creates a new tenant and an admin user.")
    public ResponseEntity<?> registerTenant(@Valid @RequestBody RegisterTenantRequest request) {
        try {
            Tenant tenant = tenantService.registerTenant(request);
            return ResponseEntity.ok(Map.of(
                "message", "Tenant registered successfully",
                "tenantId", tenant.getId(),
                "domain", tenant.getDomain()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
