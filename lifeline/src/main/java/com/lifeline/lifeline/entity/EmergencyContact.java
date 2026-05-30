
package com.lifeline.lifeline.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "emergency_contacts")
public class EmergencyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contactName;

    private String relation;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    // renamed column to avoid SQL reserved word "primary"
    @Column(name = "is_primary", nullable = false, columnDefinition = "boolean default false")
    private boolean primary = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public EmergencyContact() {}

    public Long getId()                { return id; }
    public void setId(Long id)         { this.id = id; }

    public String getContactName()                 { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public String getRelation()              { return relation; }
    public void setRelation(String relation) { this.relation = relation; }

    public String getPhoneNumber()                 { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public boolean isPrimary()             { return primary; }
    public void setPrimary(boolean primary){ this.primary = primary; }

    public User getUser()          { return user; }
    public void setUser(User user) { this.user = user; }
}


