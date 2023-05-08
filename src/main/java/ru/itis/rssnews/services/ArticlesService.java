package ru.itis.rssnews.services;

import ru.itis.rssnews.models.Article;

public interface ArticlesService {
    Article getByLink(String link);

    void addArticle(Article article);
}
