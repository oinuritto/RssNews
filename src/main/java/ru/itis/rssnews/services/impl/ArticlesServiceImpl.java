package ru.itis.rssnews.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.rssnews.exceptions.NotFoundException;
import ru.itis.rssnews.models.Article;
import ru.itis.rssnews.repositories.ArticlesRepository;
import ru.itis.rssnews.services.ArticlesService;

@Service
@RequiredArgsConstructor
public class ArticlesServiceImpl implements ArticlesService {
    private final ArticlesRepository articlesRepository;

    @Override
    public Article getByLink(String link) {
        return getArticleOrElseThrow(link);
    }

    @Override
    public void addArticle(Article article) {
        articlesRepository.save(article);
    }

    private Article getArticleOrElseThrow(String link) {
        return articlesRepository.findByLink(link)
                .orElseThrow(() -> new NotFoundException("Article with link = <" + link + "> is not found"));
    }
}
