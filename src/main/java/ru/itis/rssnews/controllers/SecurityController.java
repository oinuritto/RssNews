package ru.itis.rssnews.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SecurityController {
    @RequestMapping("/403")
    public String getAccessDeniedPage() {
        return "/error/403";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errMsg", "Wrong password or username");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }
}
