package com.example.blooddonorfinder.repository;

import com.example.blooddonorfinder.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByDonorEmailOrderByDonationDateDesc(String donorEmail);
}
