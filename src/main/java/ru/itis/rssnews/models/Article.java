package ru.itis.rssnews.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String link;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String pubDate;

    private String imageLink;

    @ManyToOne()
    @JoinColumn(name = "source_id")
    private RssSource source;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "article")
    private List<Like> likes;
}
