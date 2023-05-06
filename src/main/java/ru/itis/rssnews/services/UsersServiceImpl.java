package ru.itis.rssnews.services;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.rssnews.dto.SignUpDto;
import ru.itis.rssnews.dto.UserDto;
import ru.itis.rssnews.exceptions.NotFoundException;
import ru.itis.rssnews.models.Role;
import ru.itis.rssnews.models.User;
import ru.itis.rssnews.repositories.UsersRepository;


@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

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

    private User getUserOrElseThrow(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email = <" + email + "> is not found"));
    }
}
