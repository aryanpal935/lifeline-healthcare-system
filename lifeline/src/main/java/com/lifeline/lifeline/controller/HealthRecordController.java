package com.lifeline.lifeline.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    // SHOW ADD RECORD PAGE
    @GetMapping("/health/add")
    public String showAddRecordPage(Model model) {

        model.addAttribute("healthRecord", new HealthRecord());

        return "add-record";
    }

    // SAVE RECORD
    @PostMapping("/health/save")
    public String saveRecord(
            @Valid HealthRecord healthRecord,
            BindingResult result,
            Authentication authentication,
            Model model
    ) {

        // VALIDATION CHECK
        if (result.hasErrors()) {

            System.out.println(result.getAllErrors());

            model.addAttribute("healthRecord", healthRecord);

            return "add-record";
        }

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        healthRecord.setUser(user);

        healthRecordService.saveRecord(healthRecord);

        return "redirect:/records";
    }

    // DELETE RECORD
    @GetMapping("/delete/{id}")
    public String deleteRecord(@PathVariable Long id) {

        healthRecordService.deleteRecord(id);

        return "redirect:/records";
    }
}