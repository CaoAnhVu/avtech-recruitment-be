package com.avtech.recruitment.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private String email;
    private List<String> roles;
    private String tenantId;
}
