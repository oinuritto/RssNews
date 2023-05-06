package ru.itis.rssnews.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @GetMapping("/")
    public String getMainPage() {
        return "index";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errMsg", "Wrong password or username");
        }
        return "login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }
}
