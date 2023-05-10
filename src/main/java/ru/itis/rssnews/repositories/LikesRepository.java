package ru.itis.rssnews.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.rssnews.models.Like;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface LikesRepository extends JpaRepository<Like, Like.LikeId> {
    Optional<Like> findLikeByArticleIdAndUserId(Long articleId, Long userId);
    List<Like> findAllByUserId(Long userId);
    List<Like> findAllByArticleId(Long articleId);
    int countLikesByArticleId(Long articleId);
    int countLikesByUserId(Long userId);
}
