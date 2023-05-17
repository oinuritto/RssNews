package ru.itis.rssnews.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender mailSender;
    @Value("${rss.news.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String from;

    public SimpleMailMessage constructResetTokenEmail(String token, String email) {
        String url = host + "/password/reset?token=" + token;
        String message = "To reset your password, follow the link.";
        return constructEmail("Reset Password", message + " \r\n" + url, email);
    }

    public SimpleMailMessage constructEmail(String subject, String body,
                                            String email) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(from);
        return simpleMailMessage;
    }

    public void sendMail(SimpleMailMessage simpleMailMessage) {
        mailSender.send(simpleMailMessage);
    }
}
