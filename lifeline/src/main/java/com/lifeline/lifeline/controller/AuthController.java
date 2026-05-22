package com.lifeline.lifeline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.lifeline.lifeline.dto.RegisterRequest;
import com.lifeline.lifeline.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Open Register Page
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @GetMapping("/login")
public String loginPage() {
    return "login";
}

    // Handle Registration
    @PostMapping("/register")
    public String registerUser(RegisterRequest request, Model model) {

        String result = userService.registerUser(request);

        // Success Case
        if(result.equals("User Registered Successfully")) {

            model.addAttribute(
                    "message",
                    "Account created successfully!"
            );

        }

        // Error Case
        else {

            model.addAttribute(
                    "error",
                    result
            );
        }

        return "register";
    }
}