package ru.itis.rssnews.services;

import ru.itis.rssnews.models.Category;

import java.util.List;

public interface CategoriesService {
    boolean exists(String categoryName);
    List<Category> getAll();
    Category getByName(String categoryName);
}
