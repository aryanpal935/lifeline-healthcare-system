package com.lifeline.lifeline.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "health_records")
public class HealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // WEIGHT
    @NotNull(message = "Weight is required")
    @DecimalMin(value = "20.0", message = "Weight must be at least 20 kg")
    @DecimalMax(value = "300.0", message = "Weight too high")
    private Double weight;

    // HEIGHT
    @NotNull(message = "Height is required")
    @DecimalMin(value = "50.0", message = "Height must be at least 50 cm")
    @DecimalMax(value = "300.0", message = "Height too high")
    private Double height;

    // BLOOD PRESSURE
    @NotBlank(message = "Blood pressure is required")
    @Column(name = "blood_pressure")
    private String bloodPressure;

    // SUGAR LEVEL
    @NotNull(message = "Sugar level is required")
    @DecimalMin(value = "20.0", message = "Sugar level too low")
    @DecimalMax(value = "600.0", message = "Sugar level too high")
    @Column(name = "sugar_level")
    private Double sugarLevel;

    // BLOOD GROUP
    @NotBlank(message = "Blood group is required")
    @Column(name = "blood_group")
    private String bloodGroup;

    // ALLERGIES
    private String allergies;

    // NOTES
    private String notes;

    // CREATED AT
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // USER RELATION
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // AUTO TIME
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // =========================
    // GETTERS AND SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public Double getSugarLevel() {
        return sugarLevel;
    }

    public void setSugarLevel(Double sugarLevel) {
        this.sugarLevel = sugarLevel;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}