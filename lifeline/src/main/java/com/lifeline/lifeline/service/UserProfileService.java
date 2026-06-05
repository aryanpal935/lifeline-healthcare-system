package com.lifeline.lifeline.service;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    public int calculateAge(LocalDate dob) {

        if (dob == null) {
            return 0;
        }

        return Period.between(
                dob,
                LocalDate.now()
        ).getYears();
    }

    public String getCategory(int age) {

        if (age <= 12)
            return "Child";

        if (age <= 17)
            return "Teenager";

        if (age <= 59)
            return "Adult";

        return "Senior Citizen";
    }

    public boolean isBirthdayToday(LocalDate dob) {

        if (dob == null) {
            return false;
        }

        LocalDate today = LocalDate.now();

        return dob.getMonth() == today.getMonth()
                &&
                dob.getDayOfMonth()
                        == today.getDayOfMonth();
    }
}