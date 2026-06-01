package com.lifeline.lifeline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lifeline.lifeline.entity.Appointment;
import com.lifeline.lifeline.entity.User;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    List<Appointment> findByUser(User user);

    List<Appointment> findByUserOrderByAppointmentDateAscAppointmentTimeAsc(User user);
}