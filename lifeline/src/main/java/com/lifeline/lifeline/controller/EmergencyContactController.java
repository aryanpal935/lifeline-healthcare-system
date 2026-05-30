
package com.lifeline.lifeline.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lifeline.lifeline.entity.EmergencyContact;
import com.lifeline.lifeline.entity.User;
import com.lifeline.lifeline.repository.UserRepository;
import com.lifeline.lifeline.service.EmergencyContactService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/contacts")
public class EmergencyContactController {

    @Autowired
    private EmergencyContactService emergencyContactService;

    @Autowired
    private UserRepository userRepository;

    private User getUser(Authentication auth) {
        return userRepository.findByEmail(auth.getName()).orElse(null);
    }

    // ═════════════════════════════════════════════════════════════════════
    // GET /contacts
    // ═════════════════════════════════════════════════════════════════════
    @GetMapping
    public String contactsPage(Authentication authentication, Model model) {
        User user = getUser(authentication);
        if (user == null) return "redirect:/login";

        List<EmergencyContact> contacts =
                emergencyContactService.getContactsByUser(user);

        model.addAttribute("contacts",     contacts);
        model.addAttribute("contact",      new EmergencyContact());
        model.addAttribute("contactCount", contacts.size());
        return "contacts";
    }

    // ═════════════════════════════════════════════════════════════════════
    // POST /contacts/save  — @Valid enforces @Pattern on phoneNumber
    // ═════════════════════════════════════════════════════════════════════
    @PostMapping("/save")
    public String saveContact(
            @Valid @ModelAttribute EmergencyContact contact,
            BindingResult result,
            Authentication authentication,
            RedirectAttributes ra) {

        if (result.hasErrors()) {
            String msg = result.getFieldError() != null
                    ? result.getFieldError().getDefaultMessage()
                    : "Invalid input.";
            ra.addFlashAttribute("error", "⚠ " + msg);
            return "redirect:/contacts";
        }

        User user = getUser(authentication);
        if (user == null) return "redirect:/login";

        contact.setUser(user);

        try {
            emergencyContactService.saveContact(contact);
            ra.addFlashAttribute("message", "✅ Contact saved successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "⚠ " + e.getMessage());
        }

        return "redirect:/contacts";
    }

    // ═════════════════════════════════════════════════════════════════════
    // GET /contacts/edit/{id}
    // ═════════════════════════════════════════════════════════════════════
    @GetMapping("/edit/{id}")
    public String editContactForm(
            @PathVariable Long id,
            Authentication authentication,
            Model model,
            RedirectAttributes ra) {

        User user = getUser(authentication);
        if (user == null) return "redirect:/login";

        EmergencyContact contact = emergencyContactService.getContactById(id);

        if (contact == null
                || !contact.getUser().getId().equals(user.getId())) {
            ra.addFlashAttribute("error", "Contact not found.");
            return "redirect:/contacts";
        }

        List<EmergencyContact> contacts =
                emergencyContactService.getContactsByUser(user);

        model.addAttribute("contact",      contact);
        model.addAttribute("contacts",     contacts);
        model.addAttribute("editMode",     true);
        model.addAttribute("contactCount", contacts.size());
        return "contacts";
    }

    // ═════════════════════════════════════════════════════════════════════
    // POST /contacts/update  — @Valid enforces @Pattern, always uses updateContact()
    // ═════════════════════════════════════════════════════════════════════
    @PostMapping("/update")
    public String updateContact(
            @Valid @ModelAttribute EmergencyContact contact,
            BindingResult result,
            Authentication authentication,
            RedirectAttributes ra) {

        if (result.hasErrors()) {
            String msg = result.getFieldError() != null
                    ? result.getFieldError().getDefaultMessage()
                    : "Invalid input.";
            ra.addFlashAttribute("error", "⚠ " + msg);
            return "redirect:/contacts";
        }

        User user = getUser(authentication);
        if (user == null) return "redirect:/login";

        EmergencyContact existing =
                emergencyContactService.getContactById(contact.getId());

        if (existing == null
                || !existing.getUser().getId().equals(user.getId())) {
            ra.addFlashAttribute("error", "Contact not found.");
            return "redirect:/contacts";
        }

        existing.setContactName(contact.getContactName());
        existing.setRelation(contact.getRelation());
        existing.setPhoneNumber(contact.getPhoneNumber());
        emergencyContactService.updateContact(existing);

        ra.addFlashAttribute("message", "✅ Contact updated successfully.");
        return "redirect:/contacts";
    }

    // ═════════════════════════════════════════════════════════════════════
    // GET /contacts/delete/{id}  — passes user for auto-promote logic
    // ═════════════════════════════════════════════════════════════════════
    @GetMapping("/delete/{id}")
    public String deleteContact(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes ra) {

        User user = getUser(authentication);
        EmergencyContact contact = emergencyContactService.getContactById(id);

        if (contact == null || user == null
                || !contact.getUser().getId().equals(user.getId())) {
            ra.addFlashAttribute("error", "Contact not found.");
            return "redirect:/contacts";
        }

        emergencyContactService.deleteContact(id, user);
        ra.addFlashAttribute("message", "🗑️ Contact removed.");
        return "redirect:/contacts";
    }

    // ═════════════════════════════════════════════════════════════════════
    // POST /contacts/primary/{id}
    // ═════════════════════════════════════════════════════════════════════
    @PostMapping("/primary/{id}")
    public String setPrimary(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes ra) {

        User user = getUser(authentication);
        if (user == null) return "redirect:/login";

        emergencyContactService.setPrimary(id, user);
        ra.addFlashAttribute("message", "⭐ Primary contact updated.");
        return "redirect:/contacts";
    }
}
