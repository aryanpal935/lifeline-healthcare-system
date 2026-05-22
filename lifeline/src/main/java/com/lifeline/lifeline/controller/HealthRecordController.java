package com.lifeline.lifeline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.lifeline.lifeline.entity.HealthRecord;
import com.lifeline.lifeline.entity.User;
import com.lifeline.lifeline.repository.UserRepository;
import com.lifeline.lifeline.service.HealthRecordService;

@Controller
public class HealthRecordController {

    @Autowired
    private HealthRecordService healthRecordService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/health/add")
    public String showAddRecordPage(Model model) {

        model.addAttribute("healthRecord", new HealthRecord());

        return "add-record";
    }

    @PostMapping("/health/save")
    public String saveHealthRecord(
            HealthRecord healthRecord,
            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        healthRecord.setUser(user);

        healthRecordService.saveRecord(healthRecord);

        return "redirect:/dashboard";
    }
}