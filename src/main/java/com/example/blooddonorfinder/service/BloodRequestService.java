package com.example.blooddonorfinder.service;

import com.example.blooddonorfinder.entity.BloodRequest;
import com.example.blooddonorfinder.entity.Donor;
import com.example.blooddonorfinder.repository.BloodRequestRepository;
import com.example.blooddonorfinder.repository.DonorRepository;
import com.example.blooddonorfinder.util.BloodCompatibility;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BloodRequestService {

    // minimum days required between donations for a donor to be considered eligible again
    private static final int ELIGIBILITY_GAP_DAYS = 90;

    private final BloodRequestRepository bloodRequestRepository;
    private final DonorRepository donorRepository;

    public BloodRequestService(BloodRequestRepository bloodRequestRepository,
                                DonorRepository donorRepository) {
        this.bloodRequestRepository = bloodRequestRepository;
        this.donorRepository = donorRepository;
    }

    public BloodRequest createRequest(BloodRequest request) {
        if (request.getStatus() == null) {
            request.setStatus("PENDING");
        }
        return bloodRequestRepository.save(request);
    }

    public List<BloodRequest> getAllRequests() {
        return bloodRequestRepository.findAll();
    }

    public BloodRequest getById(Long id) {
        return bloodRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found with id: " + id));
    }

    /**
     * Matching pipeline for a request:
     *   1. narrow to donors in the same city
     *   2. keep only those currently available
     *   3. keep only those whose blood group is compatible with what's needed
     *   4. keep only those medically eligible to donate again
     *   5. rank the survivors, most "ready" donor first
     */
    public List<Donor> findMatchingDonors(BloodRequest request) {
        List<Donor> candidates = donorRepository.findByCityIgnoreCase(request.getCity());

        return candidates.stream()
                .filter(Donor::isAvailable)
                .filter(donor -> BloodCompatibility.isCompatible(donor.getBloodGroup(), request.getBloodGroupNeeded()))
                .filter(this::isEligible)
                .sorted(rankByReadiness())
                .collect(Collectors.toList());
    }

    private boolean isEligible(Donor donor) {
        LocalDate lastDonation = donor.getLastDonationDate();
        if (lastDonation == null) {
            return true; // never donated before -> eligible
        }
        long daysSinceLastDonation = ChronoUnit.DAYS.between(lastDonation, LocalDate.now());
        return daysSinceLastDonation >= ELIGIBILITY_GAP_DAYS;
    }

    /**
     * Donors who have gone longest without donating (or never donated, sorted first via
     * nullsFirst) are considered most "due" and rank highest.
     */
    private Comparator<Donor> rankByReadiness() {
        return Comparator.comparing(
                Donor::getLastDonationDate,
                Comparator.nullsFirst(Comparator.naturalOrder())
        );
    }
}
