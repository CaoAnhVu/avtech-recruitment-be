package com.avtech.recruitment.application.services;

import com.avtech.recruitment.domain.identity.User;
import com.avtech.recruitment.domain.recruitment.Candidate;
import com.avtech.recruitment.infrastructure.persistence.CandidateRepository;
import com.avtech.recruitment.infrastructure.persistence.UserRepository;
import com.avtech.recruitment.infrastructure.security.JwtTokenProvider;
import com.avtech.recruitment.infrastructure.security.UserPrincipal;
import com.avtech.recruitment.presentation.dto.AuthRequestDto;
import com.avtech.recruitment.presentation.dto.AuthResponseDto;
import com.avtech.recruitment.presentation.dto.RegisterCandidateRequestDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.jwt.expiration-ms:86400000}")
    private Long jwtExpirationMs;

    public AuthResponseDto authenticateUser(AuthRequestDto loginRequest) {
        // Authenticate the user - this uses CustomUserDetailsService to load user from DB
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate Token
        String jwt = tokenProvider.generateToken(authentication);

        // Extract Details
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return AuthResponseDto.builder()
                .accessToken(jwt)
                .tokenType("Bearer")
                .expiresIn(jwtExpirationMs)
                .email(userPrincipal.getUsername())
                .tenantId(userPrincipal.getTenantId())
                .roles(roles)
                .build();
    }

    @Transactional
    public void registerCandidate(RegisterCandidateRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(User.UserRole.ROLE_CANDIDATE)
                .active(true)
                .build();
        
        // Ensure candidate user does not belong to a specific tenant initially
        user.setTenantId("system");

        user = userRepository.save(user);

        Candidate candidate = Candidate.builder()
                .user(user)
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .build();
        
        candidate.setTenantId("system");

        candidateRepository.save(candidate);
    }
}
