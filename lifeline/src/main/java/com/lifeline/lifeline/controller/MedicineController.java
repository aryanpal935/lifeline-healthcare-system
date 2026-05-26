package com.lifeline.lifeline.controller;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lifeline.lifeline.entity.Medicine;
import com.lifeline.lifeline.entity.User;
import com.lifeline.lifeline.repository.UserRepository;
import com.lifeline.lifeline.service.MedicineService;

@Controller
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private UserRepository userRepository;

    // =========================
    // SHOW PAGE
    // =========================

    @GetMapping("/medicines")
    public String medicinesPage(
            Model model,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        var medicines =
                medicineService.getMedicinesByUser(user);

        model.addAttribute(
                "medicines",
                medicines != null ? medicines : new ArrayList<>());

        model.addAttribute("medicine", new Medicine());

        return "medicines";
    }

    // =========================
    // SAVE / UPDATE
    // =========================

    @PostMapping("/medicines/save")
    public String saveMedicine(
            @ModelAttribute Medicine medicine,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        // VALIDATION
        if (medicine.getStartDate() == null
                || medicine.getEndDate() == null) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Dates cannot be empty");

            return "redirect:/medicines";
        }

        if (medicine.getEndDate()
                .isBefore(medicine.getStartDate())) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "End date cannot be before start date");

            return "redirect:/medicines";
        }

        try {

            String email = authentication.getName();

            User user = userRepository
                    .findByEmail(email)
                    .orElse(null);

            medicine.setUser(user);

            // AUTO STATUS
            if (medicine.getEndDate().isBefore(LocalDate.now())) {

                medicine.setStatus("COMPLETED");

            } else {

                medicine.setStatus("ACTIVE");
            }

            medicineService.saveMedicine(medicine);

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Medicine saved successfully!");

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed to save medicine");
        }

        return "redirect:/medicines";
    }

    // =========================
    // EDIT
    // =========================

    @GetMapping("/medicines/edit/{id}")
    public String editMedicine(
            @PathVariable Long id,
            Model model,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        Medicine medicine =
                medicineService.getMedicineById(id);

        // SECURITY CHECK
        if (medicine == null
                || medicine.getUser() == null
                || !medicine.getUser().getId().equals(user.getId())) {

            return "redirect:/medicines";
        }

        var medicines =
                medicineService.getMedicinesByUser(user);

        model.addAttribute("medicine", medicine);

        model.addAttribute(
                "medicines",
                medicines != null ? medicines : new ArrayList<>());

        return "medicines";
    }

    // =========================
    // DELETE
    // =========================

    @GetMapping("/medicines/delete/{id}")
    public String deleteMedicine(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        Medicine medicine =
                medicineService.getMedicineById(id);

        // SECURITY CHECK
        if (medicine == null
                || medicine.getUser() == null
                || !medicine.getUser().getId().equals(user.getId())) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Unauthorized action");

            return "redirect:/medicines";
        }

        medicineService.deleteMedicine(id);

        redirectAttributes.addFlashAttribute(
                "message",
                "Medicine deleted successfully!");

        return "redirect:/medicines";
    }
}