package ru.itis.rssnews.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.rssnews.models.Article;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticlesPage {
    private List<Article> articles;
    private Integer totalPagesCount;
}
