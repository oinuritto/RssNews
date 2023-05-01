package ru.itis.rssnews.dto.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.rssnews.dto.UserDto;
import ru.itis.rssnews.models.User;

@Component
@RequiredArgsConstructor
public class UserConverter {
    public UserDto from(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
