package ru.itis.rssnews.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itis.rssnews.dto.SignUpDto;
import ru.itis.rssnews.services.UsersService;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UsersService usersService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("signUpDto", new SignUpDto());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@Valid @ModelAttribute("signUpDto") SignUpDto signUpDto, BindingResult result) {
        if (result.hasErrors()) {
            return "registration";
        }
        usersService.register(signUpDto);
        return "redirect:/login";
    }
}

