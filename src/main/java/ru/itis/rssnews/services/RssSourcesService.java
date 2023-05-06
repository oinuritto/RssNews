package ru.itis.rssnews.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.rssnews.repositories.ArticlesRepository;
import ru.itis.rssnews.repositories.RssSourcesRepositoryClearJpa;

@Service
@RequiredArgsConstructor
public class RssSourcesService {
    private final ArticlesRepository articlesRepository;
    private final RssSourcesRepositoryClearJpa sourcesRepository;

    public void deleteById(Long sourceId) {
        articlesRepository.deleteArticlesBySourceId(sourceId);
        sourcesRepository.deleteBySourceId(sourceId);
    }
}
