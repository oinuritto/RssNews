package ru.itis.rssnews.utils;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itis.rssnews.exceptions.NotFoundException;
import ru.itis.rssnews.models.Article;
import ru.itis.rssnews.models.RssSource;
import ru.itis.rssnews.services.ArticlesService;
import ru.itis.rssnews.services.RssSourcesService;

import java.util.List;

@RequiredArgsConstructor
@Component
public class NewsUpdater {
    private final ArticlesParser articlesParser;
    private final ArticlesService articlesService;
    private final RssSourcesService rssSourcesService;

    private static final Logger logger = LoggerFactory.getLogger(NewsUpdater.class);

    @Async
//    @Scheduled(fixedRate = 2 * 60 * 60 * 1000) // Задача выполняется каждые 2 часа
    @Scheduled(fixedRate = 2 * 60 * 1000) // Задача выполняется каждые 2 минуты
    public void updateNews() {
        logger.info("Started updating news.");

        // обновление новостей в БД
        try {
            List<RssSource> sources = rssSourcesService.getAllSources();
            System.out.println(sources);

            for (RssSource source: sources) {
                List<Article> articles = articlesParser.parse(source); // Парсинг новостей из XML-документа
                // Обновление новостей в БД
                for (Article article : articles) {
                    try {
                        articlesService.getByLink(article.getLink());
                    } catch (NotFoundException ex) {
                        articlesService.addArticle(article);
                    }
                }
            }
            logger.info("Updating news end.");

        } catch (Exception ex) {
            logger.error("Error updating news.", ex);
        }
    }
}
