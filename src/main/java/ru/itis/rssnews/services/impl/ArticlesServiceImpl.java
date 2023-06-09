package ru.itis.rssnews.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.itis.rssnews.dto.page.ArticlesPage;
import ru.itis.rssnews.exceptions.NotFoundException;
import ru.itis.rssnews.models.Article;
import ru.itis.rssnews.models.Category;
import ru.itis.rssnews.repositories.ArticlesRepository;
import ru.itis.rssnews.repositories.CategoriesRepository;
import ru.itis.rssnews.services.ArticlesService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticlesServiceImpl implements ArticlesService {
    private final ArticlesRepository articlesRepository;
    private final CategoriesRepository categoriesRepository;
    @Value("${articles.default.page-size}")
    private int defaultPageSize;

    @Override
    public Article getByLink(String link) {
        return getArticleOrElseThrow(link);
    }

    @Override
    public ArticlesPage getAll(int page, boolean isOrderedByLikes, Category category) {
        PageRequest pageRequest = PageRequest.of(page - 1, defaultPageSize);

        Page<Article> articlesPage;
        if (isOrderedByLikes) {
            articlesPage = category != null ?
                    articlesRepository.findAllByCategoryNameOrderByLikesDesc(category.getName(), pageRequest) :
                    articlesRepository.findAllOrderByLikesDesc(pageRequest);
        } else {
            articlesPage = category != null ?
                    articlesRepository.findAllByCategoryNameOrderByPubDateDesc(category.getName(), pageRequest) :
                    articlesRepository.findAllByOrderByPubDateDesc(pageRequest);
        }

        return ArticlesPage.builder()
                .articles(articlesPage.getContent())
                .totalPagesCount(articlesPage.getTotalPages())
                .build();
    }

    @Override
    public void addArticle(Article article) {
        Category category = article.getCategory();
        Optional<Category> foundCategory = categoriesRepository.findByName(category.getName());

        if (foundCategory.isEmpty()) {
            categoriesRepository.save(category);
        } else {
            category = foundCategory.get();
        }

        article.setCategory(category);
        articlesRepository.save(article);
    }

    @Override
    public boolean existsByLink(String link) {
        return articlesRepository.existsByLink(link);
    }

    @Override
    public ArticlesPage getAllByTitle(int page, String title) {
        PageRequest pageRequest = PageRequest.of(page - 1, defaultPageSize);
        Page<Article> articlesPage = articlesRepository.findAllByTitleContainingIgnoreCase(title, pageRequest);

        return ArticlesPage.builder()
                .articles(articlesPage.getContent())
                .totalPagesCount(articlesPage.getTotalPages())
                .build();
    }

    @Override
    public ArticlesPage getAllBySourceId(int page, Long id) {
        PageRequest pageRequest = PageRequest.of(page - 1, defaultPageSize);
        Page<Article> articlesPage = articlesRepository.findAllBySourceId(id, pageRequest);

        return ArticlesPage.builder()
                .articles(articlesPage.getContent())
                .totalPagesCount(articlesPage.getTotalPages())
                .build();
    }

    @Override
    public boolean existsById(Long id) {
        return articlesRepository.existsById(id);
    }

    private Article getArticleOrElseThrow(String link) {
        return articlesRepository.findByLink(link)
                .orElseThrow(() -> new NotFoundException("Article with link = <" + link + "> is not found"));
    }
}
