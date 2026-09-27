package com.example.blooddonorfinder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requesterName;
    private String bloodGroupNeeded;
    private String city;
    private String contactNumber;

    // Plain String by design — the checklist skips enums for this hackathon.
    // Suggested values: "PENDING", "FULFILLED", "CANCELLED"
    private String status;

    public BloodRequest() {
    }

    public BloodRequest(String requesterName, String bloodGroupNeeded, String city,
                         String contactNumber, String status) {
        this.requesterName = requesterName;
        this.bloodGroupNeeded = bloodGroupNeeded;
        this.city = city;
        this.contactNumber = contactNumber;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getBloodGroupNeeded() {
        return bloodGroupNeeded;
    }

    public void setBloodGroupNeeded(String bloodGroupNeeded) {
        this.bloodGroupNeeded = bloodGroupNeeded;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
