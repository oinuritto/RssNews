package ru.itis.rssnews.services;

import ru.itis.rssnews.models.RssSource;

import java.util.List;

public interface RssSourcesService {
    void deleteById(Long sourceId);

    List<RssSource> getAllSources();
}
