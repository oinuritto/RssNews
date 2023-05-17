package ru.itis.rssnews.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import ru.itis.rssnews.dto.SignUpDto;
import ru.itis.rssnews.dto.UpdateUserDto;
import ru.itis.rssnews.dto.UserDto;
import ru.itis.rssnews.dto.page.UsersPage;
import ru.itis.rssnews.exceptions.NotFoundException;
import ru.itis.rssnews.exceptions.UpdateEntityException;
import ru.itis.rssnews.models.PasswordResetToken;
import ru.itis.rssnews.models.helpers.Role;
import ru.itis.rssnews.models.User;
import ru.itis.rssnews.repositories.PasswordTokenRepository;
import ru.itis.rssnews.repositories.UsersRepository;
import ru.itis.rssnews.security.details.UserDetailsImpl;
import ru.itis.rssnews.services.UsersService;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final PasswordTokenRepository passwordTokenRepository;

    @Value("${users.default.page-size}")
    private int defaultPageSize;

    @Override
    public UpdateUserDto getUserForUpdateByEmail(String email) {
        User user = getUserOrElseThrow(email);
        return UpdateUserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }
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
        PageRequest pageRequest = PageRequest.of(page - 1, defaultPageSize);
        Page<User> usersPage = usersRepository.findAllByOrderByIdAsc(pageRequest);

        return UsersPage.builder()
                .users(UserDto.from(usersPage.getContent()))
                .totalPagesCount(usersPage.getTotalPages())
                .build();
    }

    @Override
    public UserDto updateUser(String email, UpdateUserDto userDto) {
        User user = getUserOrElseThrow(email);

        if (!user.getEmail().equals(userDto.getEmail())) {
            if (usersRepository.findByEmail(userDto.getEmail()).isPresent()) {
                throw new DuplicateKeyException("User with this email already exists.");
            }
        }

        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        if (userDto.getPassword() != null && !userDto.getPassword().equals("")) {
            if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
                throw new UpdateEntityException("Passwords must be equal.");
            }
            if (userDto.getPassword().length() < 6 || userDto.getPassword().length() > 32) {
                throw new UpdateEntityException("Password length must be in range(6, 32)");
            }

            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        user = usersRepository.save(user);

        if (!email.equals(user.getEmail())) {
            updateAuthenticationForUser(user);
        }
        return UserDto.from(user);
    }

    @Override
    public void deleteUser(String email) {
        User user = getUserOrElseThrow(email);
        deleteUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserOrElseThrow(id);
        deleteUser(user);
    }

    @Override
    public User getCurrentUser() {
        return getUserOrElseThrow(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public void updateUserRole(Long id, Role role) {
        User user = getUserOrElseThrow(id);
        user.setRole(role);
        usersRepository.save(user);
    }

    @Override
    public List<UserDto> getUsersByFirstNameAndLastName(String firstName, String lastName) {
        return UserDto.from(usersRepository
                .findByFirstNameStartingWithIgnoreCaseAndLastNameStartingWithIgnoreCase(firstName, lastName));
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return usersRepository.existsUserByEmail(email);
    }

    @Override
    public void createPasswordResetToken(String email, String token) {
        PasswordResetToken newToken = new PasswordResetToken(getUserOrElseThrow(email), token);
        passwordTokenRepository.save(newToken);
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return passwordTokenRepository.findUserByToken(token);
    }

    @Override
    public void updateUserPassword(String email, String password, String token) {
        User user = getUserOrElseThrow(email);
        user.setPassword(passwordEncoder.encode(password));
        usersRepository.save(user);
        passwordTokenRepository.deleteByToken(token);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

    private User getUserOrElseThrow(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email = <" + email + "> is not found"));
    }

    private User getUserOrElseThrow(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id = <" + id + "> is not found"));
    }

    private void updateAuthenticationForUser(User user) {
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
    }

    private void deleteUser(User user) {
        User currentUser = getCurrentUser();

        usersRepository.deleteById(user.getId());

        if (currentUser.equals(user)) {
            // Получаем текущий контекст безопасности
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            // Если пользователь был аутентифицирован, то очищаем его данные аутентификации
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
        }
    }
}
