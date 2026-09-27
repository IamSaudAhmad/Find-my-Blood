package com.example.blooddonorfinder.service;

import com.example.blooddonorfinder.entity.Donor;
import com.example.blooddonorfinder.repository.DonorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DonorService {

    private final DonorRepository donorRepository;

    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public Donor register(Donor donor) {
        return donorRepository.save(donor);
    }

    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    public Optional<Donor> getById(Long id) {
        return donorRepository.findById(id);
    }

    public List<Donor> searchByBloodGroupAndCity(String bloodGroup, String city) {
        return donorRepository.findByBloodGroupAndCityAndAvailableTrue(bloodGroup, city);
    }

    public List<Donor> searchByCity(String city) {
        return donorRepository.findByCityIgnoreCase(city);
    }

    public List<Donor> searchByBloodGroup(String bloodGroup) {
        return donorRepository.findByBloodGroupIgnoreCase(bloodGroup);
    }

    public Donor toggleAvailability(Long id) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Donor not found with id: " + id));
        donor.setAvailable(!donor.isAvailable());
        return donorRepository.save(donor);
    }

    public void delete(Long id) {
        donorRepository.deleteById(id);
    }
}
