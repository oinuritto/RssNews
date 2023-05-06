package ru.itis.rssnews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.rssnews.models.Role;
import ru.itis.rssnews.models.User;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private Role role;

    public static UserDto from(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }

    public static List<UserDto> from(List<User> users) {
        return users
                .stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }
}
