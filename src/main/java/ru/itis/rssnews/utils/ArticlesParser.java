package ru.itis.rssnews.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.itis.rssnews.models.Article;
import ru.itis.rssnews.models.RssSource;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArticlesParser {
    public List<Article> parse(RssSource source) throws IOException {
        List<Article> newsItems = new ArrayList<>();
        Document doc = Jsoup.parse(new URL(source.getSource()).openStream(), "UTF-8", "",
                Parser.xmlParser());


        Elements items = doc.select("item");
        for (Element item : items) {
            String title = item.select("title").first().text();
            String link = item.select("link").first().text();
            String description = item.select("description").first().text();
            String pubDate = item.select("pubDate").first().text();
            String category = item.select("category").first().text();
            String imageLink = item.select("enclosure").attr("url");

            Article newsItem = Article.builder()
                    .title(title)
                    .link(link)
                    .description(description)
                    .pubDate(pubDate)
                    .category(category)
                    .imageLink(!imageLink.equals("") ? imageLink : null)
                    .source(source)
                    .build();

            newsItems.add(newsItem);
        }
        return newsItems;
    }
}
