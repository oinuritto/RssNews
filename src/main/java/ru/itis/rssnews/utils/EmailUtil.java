package ru.itis.rssnews.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;

@Log4j2
@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final SpringTemplateEngine templateEngine;
    @Value("${elastice.mail.username}")
    private String from;
    @Value("${elastice.mail.apikey}")
    private String apiKey;
    @Value("${elastice.mail.url}")
    private String apiUrl;

    public void sendEmail(String to, String subject, String templateName, Context context)  {
        String htmlContent = templateEngine.process(templateName, context);

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("apikey", apiKey)
                .addFormDataPart("subject", subject)
                .addFormDataPart("from", from)
                .addFormDataPart("to", to)
                .addFormDataPart("bodyHtml", htmlContent)
                .build();

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                log.info("Mail sent successfully to: " + to);
            }
        } catch (IOException e) {
            log.warn("Failed to send mail to: " + to, e);
        }
    }
}

