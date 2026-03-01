package com.avtech.recruitment.infrastructure.persistence;

import com.avtech.recruitment.domain.recruitment.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String>, JpaSpecificationExecutor<Application> {
    
    // Find applications for a specific job (HR view) - Filtered by Tenant implicitly
    Page<Application> findByJobId(String jobId, Pageable pageable);
    
    // Explicit lookup without Pagination backing Drag-and-Drop Columns 
    List<Application> findByJobIdOrderByCreatedAtDesc(String jobId);
    
    // Find applications by a candidate (Candidate view)
    // Note: If candidate applies to jobs across tenants, we might need to ignore tenant filter here?
    // Or if `Application` table has `tenant_id` (from Job's tenant), then standard filter works.
    // BUT candidate viewing their own applications needs to see across tenants.
    // Strategy: Candidate's access should probably be tenant-agnostic or we query purely by candidate_id.
    List<Application> findByCandidateId(String candidateId);

    boolean existsByJobIdAndCandidateId(String jobId, String candidateId);
}
