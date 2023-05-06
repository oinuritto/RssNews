package ru.itis.rssnews.utils;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itis.rssnews.models.Article;
import ru.itis.rssnews.models.RssSource;
import ru.itis.rssnews.repositories.ArticlesRepository;
import ru.itis.rssnews.repositories.RssSourcesRepositoryClearJpa;

import java.util.List;

@RequiredArgsConstructor
@Component
public class NewsUpdater {
    private final ArticlesParser articlesParser;
    private final ArticlesRepository articlesRepository;
    private final RssSourcesRepositoryClearJpa sourcesRepository;

    private static final Logger logger = LoggerFactory.getLogger(NewsUpdater.class);

    @Async
//    @Scheduled(fixedRate = 2 * 60 * 60 * 1000) // Задача выполняется каждые 2 часа
    @Scheduled(fixedRate = 2 * 60 * 1000) // Задача выполняется каждые 2 минуты
    public void updateNews() {
        logger.info("Started updating news.");

        // обновление новостей в БД
        try {
            List<RssSource> sources = sourcesRepository.findAll();
            System.out.println(sources);

            for (RssSource source: sources) {
                List<Article> articles = articlesParser.parse(source); // Парсинг новостей из XML-документа
                // Обновление новостей в БД
                for (Article article : articles) {
                    if (articlesRepository.findByLink(article.getLink()).isEmpty()) {
                        articlesRepository.save(article);
                    }
                }
            }
            logger.info("Updating news end.");

        } catch (Exception ex) {
            logger.error("Error updating news.", ex);
        }
    }
}
