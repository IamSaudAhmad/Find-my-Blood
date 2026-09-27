package com.example.blooddonorfinder.controller;

import com.example.blooddonorfinder.entity.Donor;
import com.example.blooddonorfinder.service.DonorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @PostMapping
    public ResponseEntity<Donor> register(@RequestBody Donor donor) {
        return ResponseEntity.ok(donorService.register(donor));
    }

    @GetMapping
    public ResponseEntity<List<Donor>> getAll() {
        return ResponseEntity.ok(donorService.getAllDonors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donor> getById(@PathVariable Long id) {
        return donorService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Donor>> search(
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) String city) {

        if (bloodGroup != null && city != null) {
            return ResponseEntity.ok(donorService.searchByBloodGroupAndCity(bloodGroup, city));
        } else if (city != null) {
            return ResponseEntity.ok(donorService.searchByCity(city));
        } else if (bloodGroup != null) {
            return ResponseEntity.ok(donorService.searchByBloodGroup(bloodGroup));
        }
        return ResponseEntity.ok(donorService.getAllDonors());
    }

    @PatchMapping("/{id}/toggle-availability")
    public ResponseEntity<Donor> toggleAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(donorService.toggleAvailability(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        donorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
