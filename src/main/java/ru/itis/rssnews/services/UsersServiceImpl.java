package ru.itis.rssnews.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.rssnews.dto.SignUpDto;
import ru.itis.rssnews.dto.UpdatedUserDto;
import ru.itis.rssnews.dto.UserDto;
import ru.itis.rssnews.dto.UsersPage;
import ru.itis.rssnews.exceptions.NotFoundException;
import ru.itis.rssnews.models.Role;
import ru.itis.rssnews.models.User;
import ru.itis.rssnews.repositories.UsersRepository;


@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${default.page-size}")
    private int defaultPageSize;

    @Override
    public void register(SignUpDto signUpDto) {
        if (usersRepository.existsUserByEmail(signUpDto.getEmail()))
            throw new DuplicateKeyException("User with this email is exist");

        usersRepository.save(User.builder()
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
//                .password(signUpDto.getPassword())
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .role(Role.USER)
                .build());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return UserDto.from(getUserOrElseThrow(email));
    }

    @Override
    public UsersPage getAllUsers(int page) {
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
        Page<User> usersPage = usersRepository.findAllByOrderByIdAsc(pageRequest);

        return UsersPage.builder()
                .users(UserDto.from(usersPage.getContent()))
                .totalPagesCount(usersPage.getTotalPages())
                .build();
    }

    @Override
    public UserDto updateUser(String email, UpdatedUserDto userDto) {
        User user = getUserOrElseThrow(email);

        user.setFirstName(userDto.getFirstName());
        user.setLastName(user.getLastName());
        user.setRole(userDto.getRole());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        user = usersRepository.save(user);
        return UserDto.from(user);
    }

    @Override
    public void deleteUser(String email) {
        User user = getUserOrElseThrow(email);
        usersRepository.deleteById(user.getId());
    }

    private User getUserOrElseThrow(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email = <" + email + "> is not found"));
    }
}
