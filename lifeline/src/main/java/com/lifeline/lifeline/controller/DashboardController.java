package com.lifeline.lifeline.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    public String dashboard(Authentication authentication,
                            Model model) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        // latest record
        HealthRecord latestRecord =
                healthRecordService.getLatestRecord(user);

        // all records
        List<HealthRecord> records =
                healthRecordService.getRecordsByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("latestRecord", latestRecord);
        model.addAttribute("records", records);

        // total records
        model.addAttribute(
                "recordCount",
                healthRecordService.countRecords(user)
        );

        // BMI Logic
        if (latestRecord != null) {

            double heightMeter =
                    latestRecord.getHeight() / 100.0;

            double bmi =
                    latestRecord.getWeight()
                            / (heightMeter * heightMeter);

            String bmiCategory =
                    healthRecordService.getBmiCategory(bmi);

            model.addAttribute("bmi", bmi);
            model.addAttribute("bmiCategory", bmiCategory);
        }

        return "dashboard";
    }

    @GetMapping("/records")
    public String recordsPage(Authentication authentication,
                              Model model) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        List<HealthRecord> records =
                healthRecordService.getRecordsByUser(user);

        model.addAttribute("records", records);

        return "records";
    }
}