package com.avtech.recruitment.presentation.controllers;

import com.avtech.recruitment.domain.identity.User;
import com.avtech.recruitment.infrastructure.persistence.UserRepository;
import com.avtech.recruitment.infrastructure.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Endpoints for managing user accounts")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    @Operation(summary = "Get Current User", description = "Returns the details of the currently authenticated user.")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        Optional<User> userOpt = userRepository.findById(userPrincipal.getId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();

        // Build a safe response (without password hash)
        Map<String, Object> response = Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "role", user.getRole().name(),
                "tenantId", user.getTenantId()
        );

        return ResponseEntity.ok(response);
    }
}
