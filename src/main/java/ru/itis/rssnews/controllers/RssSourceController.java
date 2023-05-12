package ru.itis.rssnews.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.rssnews.dto.RssSourcesPage;
import ru.itis.rssnews.models.PageParam;
import ru.itis.rssnews.models.RssSource;
import ru.itis.rssnews.services.RssSourcesService;
import ru.itis.rssnews.utils.NewsUpdater;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rss")
public class RssSourceController {
    private final RssSourcesService rssSourcesService;
    private final NewsUpdater newsUpdater;

    @GetMapping
    public String getSourcesPage(@RequestParam(value = "page", defaultValue = "1") PageParam pageParam, ModelMap modelMap) {
        RssSourcesPage sourcesPage = rssSourcesService.getAllSources(pageParam.getPage());
        modelMap.put("rssSources", sourcesPage.getRssSources());
        modelMap.put("pagesCount", sourcesPage.getTotalPagesCount());
        modelMap.put("page", sourcesPage.getPageNumber());
        return "rss_sources";
    }

    @PostMapping("/delete/{id}")
    public String deleteRssSource(@PathVariable Long id) {
        rssSourcesService.deleteById(id);
        return "redirect:/rss";
    }

    @PostMapping("/add")
    public String addRssSource(@RequestParam("url") String url, RedirectAttributes attributes) {
        boolean isValid = rssSourcesService.isValidRss(url);

        if (!isValid) {
            attributes.addFlashAttribute("message", "Not valid RSS");
            return "redirect:/rss";
        }
        RssSource rssSource = RssSource.builder()
                .source(url)
                .build();
        try {
            rssSourcesService.saveSource(rssSource);
            attributes.addFlashAttribute("message", "Successfully added");
            newsUpdater.updateNews(rssSource);
        } catch (DataIntegrityViolationException ex) {
            attributes.addFlashAttribute("message", "Already saved");
        }
        return "redirect:/rss";
    }
}
