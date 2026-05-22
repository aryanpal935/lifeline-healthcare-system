package com.lifeline.lifeline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lifeline.lifeline.entity.HealthRecord;
import com.lifeline.lifeline.entity.User;

public interface HealthRecordRepository
        extends JpaRepository<HealthRecord, Long> {

    // Latest health record
    HealthRecord findTopByUserOrderByCreatedAtDesc(User user);

    // All records of a user
    List<HealthRecord> findByUser(User user);
}