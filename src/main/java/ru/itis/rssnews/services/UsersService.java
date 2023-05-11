package ru.itis.rssnews.services;

import ru.itis.rssnews.dto.SignUpDto;
import ru.itis.rssnews.dto.UpdateUserDto;
import ru.itis.rssnews.dto.UserDto;
import ru.itis.rssnews.dto.UsersPage;
import ru.itis.rssnews.models.Role;
import ru.itis.rssnews.models.User;

import java.util.List;

public interface UsersService {
    UpdateUserDto getUserForUpdateByEmail(String email);

    void register(SignUpDto signUpDto);

    UserDto getUserByEmail(String email);

    UsersPage getAllUsers(int page);

    UserDto updateUser(String email, UpdateUserDto userDto);
    void deleteUser(String email);

    void deleteUser(Long id);

    User getCurrentUser();

    void updateUserRole(Long id, Role role);

    List<UserDto> getUsersByFirstNameAndLastName(String firstName, String lastName);
}
