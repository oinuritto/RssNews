package ru.itis.rssnews.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "likes")
public class Like {

    @EmbeddedId
    private LikeId id;

    @ManyToOne
    @MapsId("user_id")
    private User user;

    @ManyToOne
    @MapsId("article_id")
    private Article article;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Embeddable
    public static class LikeId implements Serializable {
        @Column(name = "user_id")
        private Long userId;

        @Column(name = "article_id")
        private Long articleId;
    }

}

