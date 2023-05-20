package ru.itis.rssnews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.rssnews.models.Like;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeDto {
    private Long userId;
    private Long articleId;

    public static LikeDto from(Like like) {
        return LikeDto.builder()
                .userId(like.getUser().getId())
                .articleId(like.getArticle().getId())
                .build();
    }

    public static List<LikeDto> from(List<Like> likes) {
        return likes
                .stream()
                .map(LikeDto::from)
                .collect(Collectors.toList());
    }
}
