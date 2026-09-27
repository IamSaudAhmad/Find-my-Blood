package com.example.blooddonorfinder.config;

import com.example.blooddonorfinder.entity.Donor;
import com.example.blooddonorfinder.entity.User;
import com.example.blooddonorfinder.repository.DonorRepository;
import com.example.blooddonorfinder.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private final DonorRepository donorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(DonorRepository donorRepository, UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.donorRepository = donorRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        donorRepository.save(new Donor("Aarav Sharma", "O+", "Pune", "9876543210",
                LocalDate.now().minusMonths(5), true));
        donorRepository.save(new Donor("Priya Verma", "A+", "Pune", "9876543211",
                LocalDate.now().minusMonths(2), true));
        donorRepository.save(new Donor("Rohan Mehta", "B-", "Mumbai", "9876543212",
                null, true));
        donorRepository.save(new Donor("Sneha Iyer", "AB+", "Pune", "9876543213",
                LocalDate.now().minusDays(30), false));
        donorRepository.save(new Donor("Karan Singh", "O-", "Mumbai", "9876543214",
                LocalDate.now().minusYears(1), true));

        // Default demo account — CHANGE THIS before any real deployment.
       // userRepository.save(new User("Demo Admin", "admin@lifeline.org", "9999999999",
            //    passwordEncoder.encode("admin123")));
    }
}
