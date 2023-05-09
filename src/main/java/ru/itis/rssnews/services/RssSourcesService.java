package ru.itis.rssnews.services;

import ru.itis.rssnews.dto.RssSourcesPage;
import ru.itis.rssnews.models.RssSource;

import java.util.List;

public interface RssSourcesService {
    boolean isValidRss(String url);

    void deleteById(Long sourceId);

    void saveSource(RssSource rssSource);

    List<RssSource> getAllSources();

    RssSourcesPage getAllSources(int page);
}
