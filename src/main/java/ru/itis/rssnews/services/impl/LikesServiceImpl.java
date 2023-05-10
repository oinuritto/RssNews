package ru.itis.rssnews.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.rssnews.dto.LikeDto;
import ru.itis.rssnews.exceptions.NotFoundException;
import ru.itis.rssnews.models.Article;
import ru.itis.rssnews.models.Like;
import ru.itis.rssnews.models.User;
import ru.itis.rssnews.repositories.LikesRepository;
import ru.itis.rssnews.services.LikesService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {
    private final LikesRepository likesRepository;

    @Override
    public void addLike(Like like) {
        likesRepository.save(like);
    }

    @Override
    public void addLike(Long articleId, Long userId) {
        Like.LikeId likeId = Like.LikeId.builder()
                .articleId(articleId)
                .userId(userId)
                .build();

        User user = User.builder()
                .id(userId)
                .build();

        Article article = Article.builder()
                .id(articleId)
                .build();

        Like like = Like.builder()
                .id(likeId)
                .user(user)
                .article(article)
                .build();

        likesRepository.save(like);
    }

    @Override
    public List<LikeDto> getAllLikes() {
        return LikeDto.from(likesRepository.findAll());
    }

    @Override
    public List<LikeDto> getLikesByUserId(Long userId) {
        return LikeDto.from(likesRepository.findAllByUserId(userId));
    }

    @Override
    public List<LikeDto> getLikesByArticleId(Long articleId) {
        return LikeDto.from(likesRepository.findAllByArticleId(articleId));
    }

    @Override
    public int getLikesCountByUserId(Long userId) {
        return likesRepository.countLikesByUserId(userId);
    }

    @Override
    public int getLikesCountByArticleId(Long articleId) {
        return likesRepository.countLikesByArticleId(articleId);
    }

    @Override
    public LikeDto getLikeByArticleIdAndUserId(Long articleId, Long userId) {
        return LikeDto.from(getLikeOrElseThrow(articleId, userId));
    }

    @Override
    public void deleteLikeByArticleIdAndUserId(Long articleId, Long userId) {
        Like like = getLikeOrElseThrow(articleId, userId);
        likesRepository.deleteById(like.getId());
    }

    private Like getLikeOrElseThrow(Long articleId, Long userId) {
        return likesRepository.findLikeByArticleIdAndUserId(articleId, userId)
                .orElseThrow(() -> new NotFoundException("Like not found."));
    }
}
