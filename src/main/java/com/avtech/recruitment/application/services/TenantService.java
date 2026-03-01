package com.avtech.recruitment.application.services;

import com.avtech.recruitment.application.dto.tenant.RegisterTenantRequest;
import com.avtech.recruitment.domain.identity.User;
import com.avtech.recruitment.domain.tenant.Tenant;
import com.avtech.recruitment.infrastructure.persistence.TenantRepository;
import com.avtech.recruitment.infrastructure.persistence.UserRepository;
import com.avtech.recruitment.infrastructure.tenant.TenantContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Tenant registerTenant(RegisterTenantRequest request) {
        // 1. Validation
        if (tenantRepository.existsByDomain(request.getDomain())) {
            throw new IllegalArgumentException("Domain already registered.");
        }
        if (userRepository.existsByEmail(request.getAdminEmail())) {
            throw new IllegalArgumentException("Admin email already exists.");
        }

        // 2. Create Tenant
        Tenant tenant = Tenant.builder()
                .name(request.getCompanyName())
                .domain(request.getDomain())
                .status(Tenant.TenantStatus.ACTIVE)
                .planId(request.getPlanId() != null ? request.getPlanId() : "FREE")
                .build();
        
        // We must save the tenant first to generate the ID
        // IMPORTANT: For the *creation* of the tenant itself, we might be in a 'public' context
        // or a system admin context. 
        // We set the TenantContext to the new ID so the User creation is linked correctly if using @TenantId logic,
        // BUT Tenant entity itself usually lives in 'public' or has ID as PK.
        // Let's assume Tenant entity is NOT multi-tenant (it's the catalogue of tenants).
        // So we might need to override BaseEntity behavior for Tenant, or set tenantId to "system".
        
        // For simplicity in this MVP, we explicitly set tenantId to "system" for the Tenant record itself.
        tenant.setTenantId("system"); 
        tenant = tenantRepository.save(tenant);

        // 3. Create Admin User
        // Now set context to the new tenant so the User is saved with correct tenant_id
        String originalContext = TenantContext.getTenantId();
        try {
            TenantContext.setTenantId(tenant.getId());
            
            User admin = User.builder()
                    .email(request.getAdminEmail())
                    .passwordHash(passwordEncoder.encode(request.getAdminPassword()))
                    .role(User.UserRole.ROLE_HR) // Tenant Admin
                    .active(true)
                    .build();
            
            // Explicitly set tenantId if BaseEntity prePersist doesn't pick it up from ThreadLocal (it should)
             admin.setTenantId(tenant.getId());

            userRepository.save(admin);
        } finally {
            if (originalContext != null) {
                TenantContext.setTenantId(originalContext);
            } else {
                TenantContext.clear();
            }
        }

        return tenant;
    }
}
