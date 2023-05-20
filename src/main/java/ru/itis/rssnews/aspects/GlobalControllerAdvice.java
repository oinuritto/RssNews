package ru.itis.rssnews.aspects;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.itis.rssnews.models.User;
import ru.itis.rssnews.services.UsersService;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final UsersService usersService;

    @ModelAttribute("currentUser")
    public User getCurrentUser(Authentication authentication) {
        if (authentication != null) {
            return usersService.getCurrentUser();
        }
        return null;
    }
}

