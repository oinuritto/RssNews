package ru.itis.rssnews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.rssnews.models.Role;
import ru.itis.rssnews.models.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatedUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;

//    public static UpdatedUserDto from(User user) {
//        return UpdatedUserDto.builder()
//                .email(user.getEmail())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .role(user.getRole())
//                .password(user.getPassword())
//                .build();
//    }
}
