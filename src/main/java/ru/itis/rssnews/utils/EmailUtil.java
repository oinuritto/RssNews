package ru.itis.rssnews.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String from;

    public void sendEmailWithTemplate(String to, String subject, String templateName, Context context) {
        String htmlContent = templateEngine.process(templateName, context);

        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setSubject(subject);
            messageHelper.setText(htmlContent, true);
            messageHelper.setTo(to);
            messageHelper.setFrom(from);
        };

        mailSender.send(preparator);

    }
}
