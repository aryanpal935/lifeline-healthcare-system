package com.lifeline.lifeline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeline.lifeline.entity.Medicine;
import com.lifeline.lifeline.entity.User;
import com.lifeline.lifeline.repository.MedicineRepository;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    public void saveMedicine(Medicine medicine) {

        medicineRepository.save(medicine);
    }

    public List<Medicine> getMedicinesByUser(User user) {

        return medicineRepository.findByUser(user);
    }

    public Medicine getMedicineById(Long id) {

        return medicineRepository
                .findById(id)
                .orElse(null);
    }

    public void deleteMedicine(Long id) {

        medicineRepository.deleteById(id);
    }
}