package ru.itis.rssnews.services;

import ru.itis.rssnews.dto.SignUpDto;
import ru.itis.rssnews.dto.UserDto;

public interface UsersService {
    void register(SignUpDto signUpDto);

    UserDto getUserByEmail(String email);
}
