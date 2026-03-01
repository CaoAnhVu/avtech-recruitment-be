package com.avtech.recruitment.domain.identity;

import com.avtech.recruitment.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private UserRole role;

    @Column(nullable = false)
    private boolean active;

    public enum UserRole {
        ROLE_ADMIN,   // System Admin
        ROLE_HR,      // Tenant Admin/HR
        ROLE_EMPLOYER,// Hiring Manager
        ROLE_CANDIDATE // Job Seeker
    }
}
