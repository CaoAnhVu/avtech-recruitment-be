package com.avtech.recruitment.infrastructure.persistence;

import com.avtech.recruitment.domain.recruitment.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, String> {

    @Query("SELECT COUNT(i) FROM Interview i " +
           "WHERE i.interviewerId = :interviewerId " +
           "AND i.status = 'SCHEDULED' " +
           "AND ((i.startTime < :endTime AND i.endTime > :startTime))")
    long countOverlappingInterviews(
            @Param("interviewerId") String interviewerId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime
    );

    List<Interview> findByApplicationId(String applicationId);
    
    List<Interview> findByInterviewerId(String interviewerId);
}
