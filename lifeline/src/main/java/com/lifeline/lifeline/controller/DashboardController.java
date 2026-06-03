package com.lifeline.lifeline.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.lifeline.lifeline.entity.Appointment;
import com.lifeline.lifeline.entity.EmergencyContact;
import com.lifeline.lifeline.entity.HealthRecord;
import com.lifeline.lifeline.entity.Medicine;
import com.lifeline.lifeline.entity.User;
import com.lifeline.lifeline.repository.UserRepository;
import com.lifeline.lifeline.service.AppointmentService;
import com.lifeline.lifeline.service.EmergencyContactService;
import com.lifeline.lifeline.service.HealthRecordService;
import com.lifeline.lifeline.service.MedicineService;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HealthRecordService healthRecordService;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private EmergencyContactService emergencyContactService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication,
                            Model model) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        // =========================
        // HEALTH RECORDS
        // =========================

        HealthRecord latestRecord =
                healthRecordService.getLatestRecord(user);

        List<HealthRecord> records =
                healthRecordService.getRecordsByUser(user);

        model.addAttribute("latestRecord", latestRecord);
        model.addAttribute("records", records);

        model.addAttribute(
                "recordCount",
                healthRecordService.countRecords(user)
        );

        // =========================
        // ACTIVE MEDICINES
        // =========================

        List<Medicine> medicines =
                medicineService.getActiveMedicines(user);

        model.addAttribute("medicines", medicines);

        model.addAttribute(
                "medicineCount",
                medicines.size()
        );

        Map<Long, Long> medicineDaysLeft =
                new HashMap<>();

        for (Medicine medicine : medicines) {

            medicineDaysLeft.put(
                    medicine.getId(),
                    medicineService.getDaysRemaining(medicine)
            );
        }

        model.addAttribute(
                "medicineDaysLeft",
                medicineDaysLeft
            );



        model.addAttribute(
        "medicineDaysLeft",
        medicineDaysLeft
);

// =========================
// MEDICINE EXPIRY ALERTS
// =========================

List<Medicine> expiringMedicines =
        medicines.stream()
                .filter(medicine -> {

                    long days =
                            medicineService.getDaysRemaining(medicine);

                    return days >= 0 && days <= 7;
                })
                .toList();

model.addAttribute(
        "expiringMedicines",
        expiringMedicines
);

model.addAttribute(
        "expiringMedicineCount",
        expiringMedicines.size()
);

// =========================
// MEDICINE REFILL REMINDERS
// =========================

List<Medicine> refillMedicines =
        medicineService.getMedicinesNeedingRefill(user);

model.addAttribute(
        "refillMedicines",
        refillMedicines
);

model.addAttribute(
        "refillCount",
        refillMedicines.size()
);


       // =========================
// UPCOMING APPOINTMENTS
// =========================

List<Appointment> appointments =
        appointmentService.getUpcomingAppointments(user);

model.addAttribute(
        "appointments",
        appointments
);

model.addAttribute(
        "appointmentCount",
        appointments.size()
);

Appointment nextAppointment =
        appointmentService.getNextAppointment(user);

model.addAttribute(
        "nextAppointment",
        nextAppointment
);

        // =========================
        // EMERGENCY CONTACTS
        // =========================

        List<EmergencyContact> emergencyContacts =
                emergencyContactService.getContactsByUser(user);

        model.addAttribute(
                "emergencyContacts",
                emergencyContacts
        );

        model.addAttribute(
                "contactCount",
                emergencyContacts.size()
        );

        EmergencyContact primaryContact =
                emergencyContactService.getPrimaryContact(user);

        model.addAttribute(
                "primaryContact",
                primaryContact
        );

        // =========================
        // BMI + HEALTH ALERTS
        // =========================

        if (latestRecord != null
                && latestRecord.getHeight() > 0
                && latestRecord.getWeight() > 0) {

            double heightMeter =
                    latestRecord.getHeight() / 100.0;

            double bmi =
                    latestRecord.getWeight()
                            / (heightMeter * heightMeter);

            String bmiCategory =
                    healthRecordService.getBmiCategory(bmi);

            model.addAttribute(
                    "bmi",
                    Math.round(bmi * 10.0) / 10.0
            );

            model.addAttribute(
                    "bmiCategory",
                    bmiCategory
            );

            String bmiAlert = "";

            if (bmi < 18.5) {

                bmiAlert =
                        "Your BMI indicates underweight. Consider improving nutrition.";

            } else if (bmi >= 25 && bmi < 30) {

                bmiAlert =
                        "Your BMI indicates overweight. Regular exercise is recommended.";

            } else if (bmi >= 30) {

                bmiAlert =
                        "High BMI risk detected. Consult a healthcare professional.";
            }

            model.addAttribute(
                    "bmiAlert",
                    bmiAlert
            );

            // =========================
            // BLOOD PRESSURE RISK
            // =========================

            String bloodPressureRisk = null;
            String sugarRisk = null;

            String bp =
                    latestRecord.getBloodPressure();

            if (bp != null && bp.contains("/")) {

                try {

                    String[] parts =
                            bp.trim().split("/");

                    int systolic =
                            Integer.parseInt(parts[0].trim());

                    int diastolic =
                            Integer.parseInt(parts[1].trim());

                    if (systolic >= 180
                            || diastolic >= 120) {

                        bloodPressureRisk = "CRISIS";

                    } else if (systolic >= 140
                            || diastolic >= 90) {

                        bloodPressureRisk = "HIGH";

                    } else if (systolic >= 130
                            || diastolic >= 80) {

                        bloodPressureRisk = "ELEVATED";

                    } else if (systolic < 90
                            || diastolic < 60) {

                        bloodPressureRisk = "LOW";
                    }

                } catch (Exception e) {

                    bloodPressureRisk = null;
                }
            }

            // =========================
            // SUGAR RISK
            // =========================

            Double sugar =
                    latestRecord.getSugarLevel();

            if (sugar != null) {

                if (sugar >= 200) {

                    sugarRisk = "DIABETIC";

                } else if (sugar >= 126) {

                    sugarRisk = "HIGH";

                } else if (sugar >= 100) {

                    sugarRisk = "PREDIABETES";

                } else if (sugar < 70) {

                    sugarRisk = "LOW";
                }
            }

            model.addAttribute(
                    "bloodPressureRisk",
                    bloodPressureRisk
            );

            model.addAttribute(
                    "sugarRisk",
                    sugarRisk
            );

            model.addAttribute(
                    "bloodPressureVal",
                    latestRecord.getBloodPressure()
            );

            model.addAttribute(
                    "sugarVal",
                    latestRecord.getSugarLevel()
            );
        } // Closes: if (latestRecord != null ...)

        return "dashboard";
    } // Closes: dashboard(...) method

    @GetMapping("/records")
    public String recordsPage(Authentication authentication,
                              Model model) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        List<HealthRecord> records =
                healthRecordService.getRecordsByUser(user);

        model.addAttribute("records", records);

        return "records";
    } // Closes: recordsPage(...) method

} // Closes: DashboardController class