package ru.itis.rssnews.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.rssnews.models.RssSource;
import ru.itis.rssnews.repositories.ArticlesRepository;
import ru.itis.rssnews.repositories.RssSourcesRepositoryClearJpa;
import ru.itis.rssnews.services.RssSourcesService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RssSourcesServiceImpl implements RssSourcesService {
    private final ArticlesRepository articlesRepository;
    private final RssSourcesRepositoryClearJpa sourcesRepository;

    @Override
    public void deleteById(Long sourceId) {
        articlesRepository.deleteBySourceId(sourceId);
        sourcesRepository.deleteById(sourceId);
    }

    @Override
    public List<RssSource> getAllSources() {
        return sourcesRepository.findAll();
    }
}
