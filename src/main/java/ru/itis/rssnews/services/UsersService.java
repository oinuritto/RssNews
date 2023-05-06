package ru.itis.rssnews.services;

import ru.itis.rssnews.dto.SignUpDto;
import ru.itis.rssnews.dto.UpdatedUserDto;
import ru.itis.rssnews.dto.UserDto;
import ru.itis.rssnews.dto.UsersPage;

public interface UsersService {
    void register(SignUpDto signUpDto);

    UserDto getUserByEmail(String email);

    UsersPage getAllUsers(int page);

    UserDto updateUser(String email, UpdatedUserDto userDto);
    void deleteUser(String email);
}
