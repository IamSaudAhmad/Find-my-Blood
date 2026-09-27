package com.example.blooddonorfinder.controller;

import com.example.blooddonorfinder.entity.Donation;
import com.example.blooddonorfinder.entity.User;
import com.example.blooddonorfinder.repository.DonationRepository;
import com.example.blooddonorfinder.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final DonationRepository donationRepository;

    public AuthController(AuthService authService, DonationRepository donationRepository) {
        this.authService = authService;
        this.donationRepository = donationRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        String email = (String) body.get("email");
        String phone = (String) body.get("phone");
        String password = (String) body.get("password");
        boolean termsAccepted = Boolean.TRUE.equals(body.get("termsAccepted"));

        if (name == null || email == null || phone == null || password == null || !termsAccepted
                || name.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("success", false,
                    "message", !termsAccepted ? "You must accept the Terms & Conditions." : "All fields are required"));
        }
        try {
            User user = authService.signUp(name, email, phone, password, true);
            return ResponseEntity.ok(userMap(user));
        } catch (IllegalStateException alreadyExists) {
            return ResponseEntity.status(409).body(Map.of("success", false, "message", alreadyExists.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        boolean valid = email != null && password != null && authService.checkCredentials(email, password);
        if (valid) {
            User user = authService.getByEmail(email).orElseThrow();
            return ResponseEntity.ok(userMap(user));
        }
        return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid email or password"));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(@RequestParam String email) {
        return authService.getByEmail(email)
                .map(user -> {
                    Map<String, Object> data = userMap(user);
                    List<Donation> donations = donationRepository.findByDonorEmailOrderByDonationDateDesc(user.getEmail());
                    data.put("donations", donations);
                    data.put("nextDonationDate", donations.isEmpty() ? null : donations.get(0).getDonationDate().plusDays(90).toString());
                    return ResponseEntity.ok(data);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private Map<String, Object> userMap(User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("success", true);
        data.put("id", user.getId());
        data.put("name", user.getName());
        data.put("email", user.getEmail());
        data.put("phone", user.getPhone());
        data.put("termsAccepted", user.isTermsAccepted());
        return data;
    }
}
