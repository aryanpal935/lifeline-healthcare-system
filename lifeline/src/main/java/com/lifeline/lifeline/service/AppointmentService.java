package com.lifeline.lifeline.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeline.lifeline.entity.Appointment;
import com.lifeline.lifeline.entity.User;
import com.lifeline.lifeline.repository.AppointmentRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // SAVE / UPDATE APPOINTMENT
    public void saveAppointment(Appointment appointment) {

        appointmentRepository.save(appointment);
    }

    // GET ALL APPOINTMENTS OF USER
    public List<Appointment> getAppointmentsByUser(User user) {

        return appointmentRepository.findByUser(user);
    }

    // GET ONLY UPCOMING APPOINTMENTS
    public List<Appointment> getUpcomingAppointments(User user) {

        return appointmentRepository
                .findByUser(user)
                .stream()
                .filter(appointment ->
                        appointment.getAppointmentDate() != null &&
                        !appointment.getAppointmentDate()
                                .isBefore(LocalDate.now()))
                .toList();
    }

    // GET SINGLE APPOINTMENT
    public Appointment getAppointmentById(Long id) {

        return appointmentRepository
                .findById(id)
                .orElse(null);
    }

    // DELETE APPOINTMENT
    public void deleteAppointment(Long id) {

        appointmentRepository.deleteById(id);
    }
}