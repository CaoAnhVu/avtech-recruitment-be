package com.avtech.recruitment.presentation.controllers;

import com.avtech.recruitment.application.services.AuthService;
import com.avtech.recruitment.presentation.dto.AuthRequestDto;
import com.avtech.recruitment.presentation.dto.AuthResponseDto;
import com.avtech.recruitment.presentation.dto.RegisterCandidateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Login API to retrieve JWT Credentials")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login to the platform", description = "Pass Email and Password to receive a valid JWT Bearer context token.")
    public ResponseEntity<AuthResponseDto> authenticateUser(@Valid @RequestBody AuthRequestDto loginRequest) {
        AuthResponseDto response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-candidate")
    @Operation(summary = "Register a new Candidate", description = "Creates a new candidate account globally.")
    public ResponseEntity<?> registerCandidate(@Valid @RequestBody RegisterCandidateRequestDto registerRequest) {
        try {
            authService.registerCandidate(registerRequest);
            return ResponseEntity.ok(Map.of("message", "Candidate registered successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
