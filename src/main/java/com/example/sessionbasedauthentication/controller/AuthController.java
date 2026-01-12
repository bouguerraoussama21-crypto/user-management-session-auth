package com.example.sessionbasedauthentication.controller;

import com.example.sessionbasedauthentication.dto.RegisterRequestDTO;
import com.example.sessionbasedauthentication.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    // Display the login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    //Create a new user
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequestDTO registerDTO,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            authService.registerUser(registerDTO);
            model.addAttribute("success", "Registration successful! Please login.");
            return "redirect:/login?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerDTO", registerDTO);
            return "register";
        }
    }

    // Display the registration form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDTO", new RegisterRequestDTO("", "", "", "", "", ""));
        return "register";
    }


}
