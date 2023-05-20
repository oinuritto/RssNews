package ru.itis.rssnews.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.rssnews.models.Category;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    Optional<Category> findByName(String name);
}
