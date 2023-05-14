package ru.itis.rssnews.utils;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itis.rssnews.models.Article;
import ru.itis.rssnews.models.RssSource;
import ru.itis.rssnews.services.ArticlesService;
import ru.itis.rssnews.services.RssSourcesService;

import javax.net.ssl.SSLHandshakeException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class NewsUpdater {
    private final ArticlesParser articlesParser;
    private final ArticlesService articlesService;
    private final RssSourcesService rssSourcesService;

    private static final Logger logger = LoggerFactory.getLogger(NewsUpdater.class);

    @Async
    @Scheduled(fixedRate = 2 * 60 * 60 * 1000) // Задача выполняется каждые 2 часа
    public void updateNews() {
            List<RssSource> sources = rssSourcesService.getAllSources();

            for (RssSource source: sources) {
                updateNews(source);
            }
    }

    @Async
    public void updateNews(RssSource source) {
        logger.info("Started updating news for source: " + source.getSource());

        // обновление новостей в БД
        try {
                List<Article> articles = new ArrayList<>();

                try {
                    articles = articlesParser.parse(source); // Парсинг новостей из XML-документа
                } catch (SSLHandshakeException ex) {
                    logger.error("Cannot connect to: " + source.getSource());
                }

                // Обновление новостей в БД
                for (Article article : articles) {
                    if (!articlesService.existsByLink(article.getLink())) {
                        articlesService.addArticle(article);
                    }
                }

            logger.info("Updating news end for source: " + source.getSource());

        } catch (MalformedURLException ex) {
            logger.error("Not valid url in rss sources.");
            logger.info("Deleting this source from db. Its rss url = " + source.getSource());
            rssSourcesService.deleteById(source.getId());
            logger.info("Source deleted successfully.");
        } catch (Exception ex) {
            logger.error("Error updating news for source: " + source.getSource(), ex);
        }
    }
}
