package com.avtech.recruitment.domain.tenant;

import com.avtech.recruitment.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String domain; // e.g., "acme.com"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private TenantStatus status;

    @Column(name = "plan_id")
    private String planId; // Link to Subscription Plan

    public enum TenantStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED,
        PENDING_VERIFICATION
    }
}
