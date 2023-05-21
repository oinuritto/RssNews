package ru.itis.rssnews.services;

import ru.itis.rssnews.dto.page.ArticlesPage;
import ru.itis.rssnews.models.Article;
import ru.itis.rssnews.models.Category;

public interface ArticlesService {
    Article getByLink(String link);
    ArticlesPage getAll(int page, boolean isOrderedByLikes, Category category);
    void addArticle(Article article);
    boolean existsByLink(String link);
    ArticlesPage getAllByTitle(int page, String title);
    ArticlesPage getAllBySourceId(int page, Long id);
    boolean existsById(Long id);
}
