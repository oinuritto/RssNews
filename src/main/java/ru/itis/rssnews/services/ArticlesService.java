package ru.itis.rssnews.services;

import ru.itis.rssnews.dto.ArticlesPage;
import ru.itis.rssnews.models.Article;

public interface ArticlesService {
    Article getByLink(String link);

//    ArticlesPage getAll(int page);

    ArticlesPage getAll(int page, boolean isOrderedByLikes);

    void addArticle(Article article);
}
