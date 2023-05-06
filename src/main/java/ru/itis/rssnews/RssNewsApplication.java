package ru.itis.rssnews;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class RssNewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RssNewsApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(@Value("${password.encoder.strength}") int value) {
        return new BCryptPasswordEncoder(value);
    }

}
