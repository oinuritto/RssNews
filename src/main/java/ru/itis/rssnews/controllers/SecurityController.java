package ru.itis.rssnews.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityController {
    @RequestMapping("/403")
    public String getAccessDeniedPage() {
        return "/error/403";
    }
}
