package ru.itis.rssnews.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

public class ProfileController {
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String getProfilePage() {
        return "profile";
    }
}
