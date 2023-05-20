package ru.itis.rssnews.services;

import ru.itis.rssnews.dto.LikeDto;
import ru.itis.rssnews.models.Like;

import java.util.List;

public interface LikesService {
    void addLike(Like like);
    void addLike(Long articleId, Long userId);
    List<LikeDto> getAllLikes();
    List<LikeDto> getLikesByUserId(Long userId);
    List<LikeDto> getLikesByArticleId(Long articleId);
    int getLikesCountByUserId(Long userId);
    int getLikesCountByArticleId(Long articleId);

    LikeDto getLikeByArticleIdAndUserId(Long articleId, Long userId);

    void deleteLikeByArticleIdAndUserId(Long articleId, Long userId);
}
