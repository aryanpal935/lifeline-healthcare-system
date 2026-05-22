package com.lifeline.lifeline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeline.lifeline.entity.HealthRecord;
import com.lifeline.lifeline.entity.User;
import com.lifeline.lifeline.repository.HealthRecordRepository;

@Service
public class HealthRecordService {

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    // Save record
    public void saveRecord(HealthRecord healthRecord) {
        healthRecordRepository.save(healthRecord);
    }

    // Latest record
    public HealthRecord getLatestRecord(User user) {
        return healthRecordRepository
                .findTopByUserOrderByCreatedAtDesc(user);
    }

    // Count total records
    public long countRecords(User user) {
        return healthRecordRepository
                .findByUser(user)
                .size();
    }

    // Get all records
    public List<HealthRecord> getRecordsByUser(User user) {
        return healthRecordRepository.findByUser(user);
    }

    // Calculate BMI
    public double calculateBMI(HealthRecord record) {

        if (record == null || record.getHeight() <= 0) {
            return 0;
        }

        double heightInMeters = record.getHeight() / 100.0;

        return record.getWeight()
                / (heightInMeters * heightInMeters);
    }

    // BMI category
    public String getBMICategory(double bmi) {

        if (bmi < 18.5) {
            return "Underweight";
        }
        else if (bmi < 25) {
            return "Normal Weight";
        }
        else if (bmi < 30) {
            return "Overweight";
        }

        return "Obese";
    }
}