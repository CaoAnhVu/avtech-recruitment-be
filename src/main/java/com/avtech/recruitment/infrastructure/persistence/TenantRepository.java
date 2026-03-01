package com.avtech.recruitment.infrastructure.persistence;

import com.avtech.recruitment.domain.tenant.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, String> {
    Optional<Tenant> findByDomain(String domain);
    boolean existsByDomain(String domain);
}
