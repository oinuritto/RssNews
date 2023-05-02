package ru.itis.rssnews.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }
}
