package com.example.blooddonorfinder.controller;

import com.example.blooddonorfinder.entity.BloodRequest;
import com.example.blooddonorfinder.entity.Donor;
import com.example.blooddonorfinder.service.BloodRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class BloodRequestController {

    private final BloodRequestService bloodRequestService;

    public BloodRequestController(BloodRequestService bloodRequestService) {
        this.bloodRequestService = bloodRequestService;
    }

    @PostMapping
    public ResponseEntity<BloodRequest> create(@RequestBody BloodRequest request) {
        return ResponseEntity.ok(bloodRequestService.createRequest(request));
    }

    @GetMapping
    public ResponseEntity<List<BloodRequest>> getAll() {
        return ResponseEntity.ok(bloodRequestService.getAllRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodRequest> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bloodRequestService.getById(id));
    }

    @GetMapping("/{id}/matches")
    public ResponseEntity<List<Donor>> getMatches(@PathVariable Long id) {
        BloodRequest request = bloodRequestService.getById(id);
        return ResponseEntity.ok(bloodRequestService.findMatchingDonors(request));
    }
}
