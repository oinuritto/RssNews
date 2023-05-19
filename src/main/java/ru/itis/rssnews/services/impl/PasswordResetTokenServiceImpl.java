package ru.itis.rssnews.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.itis.rssnews.exceptions.NotFoundException;
import ru.itis.rssnews.models.PasswordResetToken;
import ru.itis.rssnews.models.User;
import ru.itis.rssnews.repositories.PasswordTokenRepository;
import ru.itis.rssnews.repositories.UsersRepository;
import ru.itis.rssnews.services.PasswordResetTokenService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private final PasswordTokenRepository passwordTokenRepository;
    private final UsersRepository usersRepository;

    @Override
    public void createPasswordResetToken(String email, String token) {
        Optional<User> user = usersRepository.findByEmail(email);
        if (user.isPresent()) {
            PasswordResetToken newToken = new PasswordResetToken(user.get(), token);
            passwordTokenRepository.save(newToken);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    @Override
    public boolean existsNonExpiredTokenByEmail(String email) {
        Optional<PasswordResetToken> passToken = passwordTokenRepository.findByUserEmailAndExpiryDateAfter(email, new Date());

        return passToken.filter(passwordResetToken -> !isTokenExpired(passwordResetToken)).isPresent();
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // Запускать каждые 24 часа
    public void removeExpiredTokens() {
        Date currentDate = new Date();
        List<PasswordResetToken> expiredTokens = passwordTokenRepository.findByExpiryDateBefore(currentDate);
        passwordTokenRepository.deleteAll(expiredTokens);
    }
    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
}
