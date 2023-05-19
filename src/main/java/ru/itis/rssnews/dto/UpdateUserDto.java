package ru.itis.rssnews.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserDto {
    @Size(min = 6, max = 32)
    @NotBlank
    private String email;

    @Size(min = 2, max = 32)
    @NotBlank
    private String firstName;

    @Size(min = 2, max = 32)
    @NotBlank
    private String lastName;

    private String password;

    private String confirmPassword;
}
