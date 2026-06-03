package com.lifeline.lifeline.service;

import java.time.LocalDate;
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

    // SAVE MEDICINE
    public void saveMedicine(Medicine medicine) {

        medicineRepository.save(medicine);
    }

    // GET USER MEDICINES
    public List<Medicine> getMedicinesByUser(User user) {

        return medicineRepository.findByUser(user);
    }

    // GET SINGLE MEDICINE
    public Medicine getMedicineById(Long id) {

        return medicineRepository
                .findById(id)
                .orElse(null);
    }

    // DELETE MEDICINE
    public void deleteMedicine(Long id) {

        medicineRepository.deleteById(id);
    }

    public long getDaysRemaining(Medicine medicine) {

    if (medicine.getEndDate() == null) {
        return -1;
    }

    return java.time.temporal.ChronoUnit.DAYS.between(
            java.time.LocalDate.now(),
            medicine.getEndDate()
    );
}

// GET ACTIVE MEDICINES ONLY
public List<Medicine> getActiveMedicines(User user) {

    LocalDate today = LocalDate.now();

    return medicineRepository.findByUser(user)
            .stream()
            .filter(medicine ->
                    medicine.getStartDate() != null &&
                    medicine.getEndDate() != null &&
                    !today.isBefore(medicine.getStartDate()) &&
                    !today.isAfter(medicine.getEndDate()) &&
                    "ACTIVE".equalsIgnoreCase(medicine.getStatus()))
            .toList();
}

}