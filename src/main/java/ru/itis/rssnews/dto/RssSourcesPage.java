package ru.itis.rssnews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.rssnews.models.RssSource;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RssSourcesPage {
    private List<RssSource> rssSources;
    private Integer pageNumber;
    private Integer totalPagesCount;
}
