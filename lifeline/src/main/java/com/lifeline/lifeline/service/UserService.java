package com.lifeline.lifeline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lifeline.lifeline.dto.RegisterRequest;
import com.lifeline.lifeline.entity.User;
import com.lifeline.lifeline.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already exists";
        }

        // Create new user
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Encrypt password
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // Set default role
        user.setRole("ROLE_USER");

        user.setDateOfBirth(request.getDateOfBirth());
user.setGender(request.getGender());
user.setPhoneNumber(request.getPhoneNumber());

        // Save user in database
        userRepository.save(user);

        return "User Registered Successfully";
    }
}