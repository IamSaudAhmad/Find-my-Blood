package com.example.blooddonorfinder.repository;

import com.example.blooddonorfinder.entity.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {

    List<BloodRequest> findByStatus(String status);

    List<BloodRequest> findByCityIgnoreCase(String city);
}
