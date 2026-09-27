package com.example.blooddonorfinder.service;

import com.example.blooddonorfinder.entity.User;
import com.example.blooddonorfinder.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signUp(String name, String email, String phone, String rawPassword, boolean termsAccepted) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("An account with this email already exists");
        }
        User user = new User(name, email, phone, passwordEncoder.encode(rawPassword), termsAccepted);
        return userRepository.save(user);
    }

    public boolean checkCredentials(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
