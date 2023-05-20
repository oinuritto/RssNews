package ru.itis.rssnews.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PasswordDto {
    private  String token;

    @Size(min = 6, max = 32)
    @NotBlank
    private String newPassword;
}
