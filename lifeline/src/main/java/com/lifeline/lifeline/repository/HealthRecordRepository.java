package com.lifeline.lifeline.repository;

import com.lifeline.lifeline.entity.HealthRecord;
import com.lifeline.lifeline.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {

    List<HealthRecord> findByUserOrderByCreatedAtDesc(User user);

}