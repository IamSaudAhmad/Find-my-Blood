package com.example.blooddonorfinder.repository;

import com.example.blooddonorfinder.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonorRepository extends JpaRepository<Donor, Long> {

    List<Donor> findByBloodGroupAndCityAndAvailableTrue(String bloodGroup, String city);

    List<Donor> findByCityIgnoreCase(String city);

    List<Donor> findByBloodGroupIgnoreCase(String bloodGroup);

    List<Donor> findByAvailableTrue();
}
