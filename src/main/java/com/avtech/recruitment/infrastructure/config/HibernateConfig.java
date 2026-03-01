package com.avtech.recruitment.infrastructure.config;

import com.avtech.recruitment.infrastructure.tenant.TenantContext;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.Map;

@Configuration
public class HibernateConfig {

    @Bean
    public CurrentTenantIdentifierResolver<String> currentTenantIdentifierResolver() {
        return new CurrentTenantIdentifierResolver<>() {
            @Override
            public String resolveCurrentTenantIdentifier() {
                String tenantId = TenantContext.getTenantId();
                return tenantId != null ? tenantId : "public";
            }

            @Override
            public boolean validateExistingCurrentSessions() {
                return true;
            }
        };
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(CurrentTenantIdentifierResolver<String> resolver) {
        return properties -> properties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, resolver);
    }
    
    // Note: For DISCRIMINATOR column strategy in Hibernate 6+, 
    // we typically use @TenantId on the entity field.
    // The "DISCRIMINATOR" strategy in older Hibernate versions is different.
    // In Hibernate 6, @TenantId is the standard way.
}
