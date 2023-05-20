package ru.itis.rssnews.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.context.Context;
import ru.itis.rssnews.dto.PasswordDto;
import ru.itis.rssnews.models.User;
import ru.itis.rssnews.services.PasswordResetTokenService;
import ru.itis.rssnews.services.UsersService;
import ru.itis.rssnews.utils.EmailUtil;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("/password")
@RequiredArgsConstructor
@Controller
public class PasswordResetController {
    private final UsersService usersService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final EmailUtil emailUtil;

    @GetMapping("/forgot")
    public String forgotPasswordPage() {
        return "password_forgot";
    }

    @PostMapping("/forgot")
    public String processForgotPassword(@RequestParam("email") String email,
                                        RedirectAttributes attributes) {

        if (!usersService.existsUserByEmail(email)) {
            attributes.addFlashAttribute("message", "User not found");
            return "redirect:/password/forgot";
        }

        if (passwordResetTokenService.existsNonExpiredTokenByEmail(email)) {
            attributes.addFlashAttribute("message",
                    "You already sent link to this email.\nFind in your mail or wait before link expires.");
            return "redirect:/password/forgot";
        }

        String token = UUID.randomUUID().toString();
        passwordResetTokenService.createPasswordResetToken(email, token);

        sendResetPasswordMail(email, token);

        attributes.addFlashAttribute("message", "The link sent to your email.");
        return "redirect:/password/forgot";
    }

    @GetMapping("/reset")
    public String resetPasswordPage(Model model,
                                         @RequestParam("token") String token,
                                         RedirectAttributes attributes) {
        String result = passwordResetTokenService.validatePasswordResetToken(token);
        if (result != null) {
            attributes.addFlashAttribute("message", "Link expired or invalid.");
            return "redirect:/password/forgot";
        } else {
            model.addAttribute("passwordDto", new PasswordDto());
            model.addAttribute("token", token);

            return "password_reset";
        }
    }

    @PostMapping("/reset")
    public String processResetPassword(@Valid @ModelAttribute("passwordDto") PasswordDto passwordDto,
                               @RequestParam("token") String token,
                               BindingResult bindingResult,
                               RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            return "password_reset";
        }
        String result = passwordResetTokenService.validatePasswordResetToken(token);

        if (result != null) {
            attributes.addFlashAttribute("message", "Link expired or invalid.");
            return "redirect:/password/forgot";
        }

        Optional<User> user = usersService.getUserByPasswordResetToken(token);
        if (user.isPresent()) {
            usersService.updateUserPassword(user.get().getEmail(), passwordDto.getNewPassword(), token);
            attributes.addFlashAttribute("message", "Password changed successful");
        } else {
            attributes.addFlashAttribute("message", "User not found...");
        }
        return "redirect:/password/forgot";
    }

    private void sendResetPasswordMail(String email, String token) {
        Context context = new Context();
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        context.setVariable("baseUrl", baseUrl);
        context.setVariable("token", token);
        emailUtil.sendEmail(email, "Password Reset", "password_reset_mail", context);
    }
}
