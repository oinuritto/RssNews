package ru.itis.rssnews.services.impl;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itis.rssnews.dto.page.RssSourcesPage;
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

    @Value("${rssSources.default.page-size}")
    private int defaultPageSize;

    @Override
    public boolean isValidRss(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            // Проверяем, что документ содержит RSS-контент
            return doc.select("rss").size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void deleteById(Long sourceId) {
        articlesRepository.deleteBySourceId(sourceId);
        sourcesRepository.deleteById(sourceId);
    }

    @Override
    public void saveSource(RssSource rssSource) {
        sourcesRepository.save(rssSource);
    }

    @Override
    public List<RssSource> getAllSources() {
        return sourcesRepository.findAll();
    }


    @Override
    public RssSourcesPage getAllSources(int page) {
        return sourcesRepository.findAll(page, defaultPageSize);
    }
}
