package com.avtech.recruitment.infrastructure.persistence;

import com.avtech.recruitment.domain.identity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // Native query bypasses the Hibernate @Filter("tenantFilter")
    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);
    
    boolean existsByEmail(String email);
}
