package ru.itis.rssnews.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.rssnews.dto.ArticlesPage;
import ru.itis.rssnews.models.PageParam;
import ru.itis.rssnews.services.ArticlesService;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ArticlesService articlesService;

    @GetMapping("/")
    public String getMainPage(@RequestParam(value = "page", defaultValue = "1") PageParam pageParam, ModelMap modelMap) {
        ArticlesPage articlesPage = articlesService.getAll(pageParam.getPage());
        modelMap.put("articles", articlesPage.getArticles());
        modelMap.put("pagesCount", articlesPage.getTotalPagesCount());
        modelMap.put("page", pageParam.getPage());
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errMsg", "Wrong password or username");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }
}
