package ru.itis.rssnews.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI rssNewsOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("RSS News")
                        .description("The news web parser from rss")
                        .version("0.1"));
    }
}
