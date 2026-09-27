package com.example.blooddonorfinder.controller;

import com.example.blooddonorfinder.entity.Donation;
import com.example.blooddonorfinder.repository.DonationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/donations")
public class DonationController {
    private final DonationRepository repository;

    public DonationController(DonationRepository repository) { this.repository = repository; }

    @GetMapping
    public ResponseEntity<List<Donation>> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(repository.findByDonorEmailOrderByDonationDateDesc(email));
    }

    @PostMapping
    public ResponseEntity<Donation> add(@RequestBody Donation donation) {
        if (donation.getDonorEmail() == null || donation.getDonorEmail().isBlank() || donation.getDonationDate() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (donation.getDonationDate().isAfter(LocalDate.now())) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(repository.save(donation));
    }
}
