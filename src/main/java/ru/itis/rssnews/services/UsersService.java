package ru.itis.rssnews.services;

import ru.itis.rssnews.dto.SignUpDto;

public interface UsersService {
    void register(SignUpDto signUpDto);
}
