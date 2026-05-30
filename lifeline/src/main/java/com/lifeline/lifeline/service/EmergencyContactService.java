
package com.lifeline.lifeline.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lifeline.lifeline.entity.EmergencyContact;
import com.lifeline.lifeline.entity.User;
import com.lifeline.lifeline.repository.EmergencyContactRepository;

@Service
public class EmergencyContactService {

    private static final int MAX_CONTACTS = 5;

    @Autowired
    private EmergencyContactRepository emergencyContactRepository;

    // ── SAVE (new contact — enforces 5-contact limit) ─────────────────────
    public void saveContact(EmergencyContact contact) throws Exception {
        long count = emergencyContactRepository.countByUser(contact.getUser());
        if (count >= MAX_CONTACTS) {
            throw new Exception(
                "Maximum " + MAX_CONTACTS + " emergency contacts allowed.");
        }
        emergencyContactRepository.save(contact);
    }

    // ── UPDATE (edit existing — limit never checked here) ─────────────────
    public void updateContact(EmergencyContact contact) {
        emergencyContactRepository.save(contact);
    }

    // ── GET ALL FOR USER ──────────────────────────────────────────────────
    public List<EmergencyContact> getContactsByUser(User user) {
        return emergencyContactRepository.findByUser(user);
    }

    // ── GET SINGLE ────────────────────────────────────────────────────────
    public EmergencyContact getContactById(Long id) {
        return emergencyContactRepository.findById(id).orElse(null);
    }

    // ── DELETE — auto-promotes first remaining contact if primary deleted ──
    @Transactional
    public void deleteContact(Long id, User user) {
        EmergencyContact target =
                emergencyContactRepository.findById(id).orElse(null);
        if (target == null) return;

        boolean wasPrimary = target.isPrimary();
        emergencyContactRepository.deleteById(id);

        if (wasPrimary) {
            List<EmergencyContact> remaining =
                    emergencyContactRepository.findByUser(user);
            if (!remaining.isEmpty()) {
                // deterministic: promote alphabetically first contact
                remaining.sort(Comparator.comparing(EmergencyContact::getContactName));
                remaining.get(0).setPrimary(true);
                emergencyContactRepository.save(remaining.get(0));
            }
        }
    }

    // ── SET PRIMARY — one bulk UPDATE, then set new ───────────────────────
    @Transactional
    public void setPrimary(Long contactId, User user) {
        emergencyContactRepository.clearPrimaryContacts(user);
        EmergencyContact contact =
                emergencyContactRepository.findById(contactId).orElse(null);
        if (contact != null
                && contact.getUser().getId().equals(user.getId())) {
            contact.setPrimary(true);
            emergencyContactRepository.save(contact);
        }
    }

    // ── GET PRIMARY — direct DB query, no stream ──────────────────────────
    public EmergencyContact getPrimaryContact(User user) {
        return emergencyContactRepository.findByUserAndPrimaryTrue(user);
    }

    // ── COUNT ─────────────────────────────────────────────────────────────
    public long countContacts(User user) {
        return emergencyContactRepository.countByUser(user);
    }
}
