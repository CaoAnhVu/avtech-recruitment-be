package com.avtech.recruitment.infrastructure.persistence;

import com.avtech.recruitment.domain.recruitment.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, String> {
    Optional<Candidate> findByUserId(String userId);
}
