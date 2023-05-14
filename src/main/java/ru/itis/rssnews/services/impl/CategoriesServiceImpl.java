package ru.itis.rssnews.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.rssnews.exceptions.NotFoundException;
import ru.itis.rssnews.models.Category;
import ru.itis.rssnews.repositories.CategoriesRepository;
import ru.itis.rssnews.services.CategoriesService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;

    @Override
    public boolean exists(String categoryName) {
        return categoriesRepository.existsByName(categoryName);
    }

    @Override
    public List<Category> getAll() {
        return categoriesRepository.findAll();
    }

    @Override
    public Category getByName(String categoryName) {
        return getCategoryOrElseThrow(categoryName);
    }

    private Category getCategoryOrElseThrow(String name) {
        return categoriesRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Category with name = <" + name + "> is not found"));
    }
}
