package com.lifeline.lifeline.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.lifeline.lifeline.entity.HealthRecord;
import com.lifeline.lifeline.entity.User;
import com.lifeline.lifeline.repository.UserRepository;
import com.lifeline.lifeline.service.HealthRecordService;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HealthRecordService healthRecordService;

    @GetMapping("/dashboard")
    public String dashboard(
            Model model,
            Principal principal
    ) {

        String email = principal.getName();

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        // Latest record
        HealthRecord latestRecord =
                healthRecordService.getLatestRecord(user);

        // Total records
        long totalRecords =
                healthRecordService.countRecords(user);

        // All records
        List<HealthRecord> records =
                healthRecordService.getRecordsByUser(user);

        // BMI
        double bmi =
                healthRecordService.calculateBMI(latestRecord);

        // BMI category
        String bmiCategory =
                healthRecordService.getBMICategory(bmi);

        // Send data to dashboard
        model.addAttribute("user", user);
        model.addAttribute("latestRecord", latestRecord);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("records", records);
        model.addAttribute("bmi", bmi);
        model.addAttribute("bmiCategory", bmiCategory);

        return "dashboard";
    }
}