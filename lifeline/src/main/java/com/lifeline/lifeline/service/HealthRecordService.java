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

    // SAVE RECORD
    public void saveRecord(HealthRecord healthRecord) {
        healthRecordRepository.save(healthRecord);
    }

    // GET ALL RECORDS OF USER
    public List<HealthRecord> getRecordsByUser(User user) {
        return healthRecordRepository.findByUserOrderByCreatedAtDesc(user);
    }

    // GET LATEST RECORD
    public HealthRecord getLatestRecord(User user) {

        List<HealthRecord> records =
                healthRecordRepository.findByUserOrderByCreatedAtDesc(user);

        if (records.isEmpty()) {
            return null;
        }

        return records.get(0);
    }

    // COUNT RECORDS
    public long countRecords(User user) {
        return healthRecordRepository
                .findByUserOrderByCreatedAtDesc(user)
                .size();
    }

    // BMI CATEGORY
    public String getBmiCategory(double bmi) {

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

    public void deleteRecord(Long id) {
    healthRecordRepository.deleteById(id);
}

}