package com.example.sessionbasedauthentication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Display the home page
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // Display the dashboard page
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}