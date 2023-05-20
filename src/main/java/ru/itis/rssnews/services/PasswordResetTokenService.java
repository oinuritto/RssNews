package ru.itis.rssnews.services;

public interface PasswordResetTokenService {
    void createPasswordResetToken(String email, String token);

    String validatePasswordResetToken(String token);

    boolean existsNonExpiredTokenByEmail(String email);
}
