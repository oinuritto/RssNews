package ru.itis.rssnews.utils;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.itis.rssnews.models.Article;
import ru.itis.rssnews.models.Category;
import ru.itis.rssnews.models.RssSource;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Log4j2
@Component
public class ArticlesParser {
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";

    public List<Article> parse(RssSource source) throws IOException {
        List<Article> newsItems = new ArrayList<>();
        Document doc = Jsoup.parse(new URL(source.getSource()).openStream(), "UTF-8", "",
                Parser.xmlParser());


        Elements items = doc.select("item");
        for (Element item : items) {
            String title = item.select("title").first().text();
            String link = item.select("link").first().text();
            String description = item.select("description").first().text();
            String category = item.select("category").first().text();
            String pubDateStr = item.select("pubDate").first().text();
            Date pubDate;

            try {
                pubDate = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(pubDateStr);
            } catch (ParseException e) {
                log.warn(String.format("Cannot parse pubDate for element with link %s in rss source %s",
                        link, source.getSource()), e);
                continue;
            }

            Elements enclosureItems = item.select("enclosure");
            String imageLink = "";
            for (Element enclosureItem: enclosureItems) {
                if (enclosureItem.attr("type").contains("image")) {
                    imageLink = enclosureItem.attr("url");
                    break;
                }
            }

            Article newsItem = Article.builder()
                    .title(title)
                    .link(link)
                    .description(description)
                    .pubDate(pubDate)
                    .category(Category.builder()
                            .name(category)
                            .build())
                    .imageLink(!imageLink.equals("") ? imageLink : null)
                    .source(source)
                    .build();

            newsItems.add(newsItem);
        }
        return newsItems;
    }
}
