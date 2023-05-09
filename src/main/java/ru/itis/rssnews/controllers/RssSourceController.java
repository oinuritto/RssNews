package ru.itis.rssnews.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.rssnews.dto.RssSourcesPage;
import ru.itis.rssnews.models.RssSource;
import ru.itis.rssnews.services.RssSourcesService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rss")
public class RssSourceController {
    private final RssSourcesService rssSourcesService;

    @GetMapping
    public String getSourcesPage(@RequestParam(value = "page", defaultValue = "1") String pageStr, ModelMap modelMap) {
        int page;
        try {
            page = Integer.parseInt(pageStr);
        } catch (NumberFormatException ex) {
            page = 1;
        }

        RssSourcesPage sourcesPage = rssSourcesService.getAllSources(page);
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
        System.out.println(url);
        System.out.println(isValid);
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
        } catch (DataIntegrityViolationException ex) {
            attributes.addFlashAttribute("message", "Already saved");
        }
        return "redirect:/rss";
    }
}
