package ru.itis.rssnews.aspects;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.itis.rssnews.dto.UserDto;
import ru.itis.rssnews.services.UsersService;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final UsersService usersService;

    @ModelAttribute("currentUser")
    public UserDto getCurrentUser(Authentication authentication) {
        if (authentication != null) {
            return usersService.getUserByEmail(authentication.getName());
        }
        return null;
    }
}

