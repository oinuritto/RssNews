package ru.itis.rssnews.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.rssnews.models.Article;

import java.util.Optional;

@Repository
@Transactional
public interface ArticlesRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByLink(String link);
    Page<Article> findAllByOrderByIdAsc(Pageable pageable);
    void deleteBySourceId(Long id);
}
