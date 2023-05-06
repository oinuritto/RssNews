package ru.itis.rssnews.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.rssnews.dto.UpdateUserDto;
import ru.itis.rssnews.exceptions.UpdateEntityException;
import ru.itis.rssnews.security.details.UserDetailsImpl;
import ru.itis.rssnews.services.UsersService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UsersService usersService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public String getProfilePage() {
        return "profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit")
    public String getProfileEditPage(@AuthenticationPrincipal UserDetailsImpl loggedUser, ModelMap modelMap) {
        modelMap.put("updateUserDto", usersService.getUserForUpdateByEmail(loggedUser.getUsername()));
        return "userEditProfile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit")
    public String editProfile(@Valid @ModelAttribute("updateUserDto") UpdateUserDto updateUserDto,
                              BindingResult result, RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal UserDetailsImpl loggedUser) {
        if (result.hasErrors()) {
            return "userEditProfile";
        }

        try {
            usersService.updateUser(loggedUser.getUsername(), updateUserDto);
        } catch (UpdateEntityException ex) {
            result.rejectValue("password", "password.error", ex.getMessage());
            return "userEditProfile";
        } catch (DuplicateKeyException ex) {
            result.rejectValue("email", "entry.duplicate", ex.getMessage());
            return "userEditProfile";
        }

        redirectAttributes.addFlashAttribute("message", "Success");
        return "redirect:/profile/edit";
    }
}
