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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lifeline.lifeline.entity.Appointment;
import com.lifeline.lifeline.entity.User;
import com.lifeline.lifeline.repository.UserRepository;
import com.lifeline.lifeline.service.AppointmentService;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserRepository userRepository;

    // =========================
    // SHOW APPOINTMENTS PAGE
    // =========================

    @GetMapping
    public String showAppointmentsPage(
            Model model,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        var appointments =
                appointmentService.getAppointmentsByUser(user);

        model.addAttribute(
                "appointments",
                appointments != null ? appointments : new ArrayList<>());

        model.addAttribute("appointment", new Appointment());

        return "appointments";
    }

    // =========================
    // SAVE / UPDATE APPOINTMENT
    // =========================

    @PostMapping("/save")
    public String saveAppointment(
            @ModelAttribute Appointment appointment,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        // DATE VALIDATION
        if (appointment.getAppointmentDate() == null
                || appointment.getAppointmentDate().isBefore(LocalDate.now())) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Appointment date cannot be in the past");

            return "redirect:/appointments";
        }

        try {

            String email = authentication.getName();

            User user = userRepository
                    .findByEmail(email)
                    .orElse(null);

            appointment.setUser(user);

            appointmentService.saveAppointment(appointment);

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Appointment saved successfully!");

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed to save appointment");
        }

        return "redirect:/appointments";
    }

    // =========================
    // EDIT APPOINTMENT
    // =========================

    @GetMapping("/edit/{id}")
    public String editAppointment(
            @PathVariable Long id,
            Model model,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        Appointment appointment =
                appointmentService.getAppointmentById(id);

        var appointments =
                appointmentService.getAppointmentsByUser(user);

        model.addAttribute("appointment", appointment);

        model.addAttribute(
                "appointments",
                appointments != null ? appointments : new ArrayList<>());

        return "appointments";
    }

    // =========================
    // DELETE APPOINTMENT
    // =========================

    @GetMapping("/delete/{id}")
    public String deleteAppointment(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {

            appointmentService.deleteAppointment(id);

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Appointment deleted successfully!");

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed to delete appointment");
        }

        return "redirect:/appointments";
    }
}