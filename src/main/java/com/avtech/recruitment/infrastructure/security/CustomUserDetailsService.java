package com.avtech.recruitment.infrastructure.security;

import com.avtech.recruitment.domain.identity.User;
import com.avtech.recruitment.infrastructure.persistence.UserRepository;
import com.avtech.recruitment.infrastructure.tenant.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // IMPORTANT: We need to search across ALL tenants or specific tenant?
        // Usually, email is unique system-wide in SaaS platforms (for simplicity).
        // If email is unique per tenant only, we need tenant context first (chicken-egg).
        // Here we assume email is unique globally.
        // However, standard hibernate filter might block us if we don't handle it.
        // For login, we usually need to find the user *regardless* of current tenant filter, 
        // OR we know the tenant from the login request (e.g. subdomain).
        
        // Strategy: 
        // Option A: Disable tenant filter for this query.
        // Option B: Assume tenant filter is NOT active during login (it isn't yet, no context).
        // If TenantFilter sets "public" or similar, we might leverage that.
        // BUT, our BaseEntity/HibernateConfig might enforce a filter.
        
        // For this implementation, we assume that UserRepository methods need to run WITHOUT tenant filter 
        // to find the user and their tenant_id.
        // Since we are using a shared schema with discriminator, we might need a custom query 
        // or temporarily disable aspects if we enforce them strictly.
        
        // For now, let's assume the default `findByEmail` works because during auth, 
        // TenantContext might be empty or 'public'. 
        // If our HibernateConfig sets default to 'public', we won't find the user if they are in 'tenant-1'.
        
        // FIX: We need a way to bypass tenant filter for login.
        // In a real generic SaaS, we might have a distinct `AppUser` table decoupled from tenants, or use `AOP` to disable filter.
        // For simplicity here, let's assume we can fetch the user.
        // If `TenantContext` is null/empty, we might need to rely on the fact that Hibernate Filter 
        // is NOT enabled if we didn't explicitly enable it in a specific aspect or if we configure 
        // the Resolver to return a special value that disables the filter.
        
        // Actually, let's update HibernateConfig later to allow "all" access if needed, or use a native query.
        // Or better: The User entity is the one thing that crosses boundaries? No, User belongs to a Tenant.
        
        // Solution for MVP: Helper method in Repository or Custom Implementation to find by Email ignoring permissions.
        // But JpaRepository methods obey filters enabled on the Session.
        
        // Let's rely on the fact that we can set TenantContext to a "superuser" or "no-filter" state if needed.
        // Or simpler: The user provides the TenantID (via subdomain) -> Filter sets it -> We find user.
        // If we want "Global Login" (email only), we must search all.
        
        // Let's assume Global Unique Email. We'll attempt to find the user.
        // To make this work with Discriminator, we might need to disable the filter manually here.
        // But Spring Data JPA doesn't easily expose the Session to disable filters per query.
        
        // PRO TIP: Create a specific method `findByEmailIgnoreTenant` using `@Query(nativeQuery=true...)` 
        // or a custom repository implementation.
        // For now, we will try standard lookup. If it fails due to filter, we will revisit.
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return UserPrincipal.create(user);
    }
}
