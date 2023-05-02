package ru.itis.rssnews.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
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
public class SignUpDto {
    @Size(min = 2, max = 32)
    @NotBlank
    private String firstName;

    @Size(min = 2, max = 32)
    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @Size(min = 6, max = 32)
    @NotBlank
    private String password;

    @AssertTrue
    private Boolean isAcceptedPolicyAgreement;
}
