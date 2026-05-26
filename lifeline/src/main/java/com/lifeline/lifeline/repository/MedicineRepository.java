package com.lifeline.lifeline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lifeline.lifeline.entity.Medicine;
import com.lifeline.lifeline.entity.User;

public interface MedicineRepository
        extends JpaRepository<Medicine, Long> {

    List<Medicine> findByUser(User user);
}