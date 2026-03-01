package com.avtech.recruitment.infrastructure.persistence;

import com.avtech.recruitment.domain.recruitment.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, String>, JpaSpecificationExecutor<Job> {
    // Multi-tenancy is handled globally by Hibernate Filter (if enabled)
    // OR we explicitly search by tenantId if using Spring Data's abstract handling.
    // Since we used @TenantId and/or Filters, standard methods usually respect it.
    
    // For PUBLIC API (Candidates searching jobs across companies), 
    // we might need a method that IGNORES the tenant filter or explicit tenantId search.
    
    // Find all OPEN jobs for a specific tenant (Public Page of a Company)
    List<Job> findByTenantIdAndStatus(String tenantId, Job.JobStatus status);
    
    // Find all OPEN jobs (Global Job Board) - This might require disabling Tenant Filter
    Page<Job> findByStatus(Job.JobStatus status, Pageable pageable);
}
