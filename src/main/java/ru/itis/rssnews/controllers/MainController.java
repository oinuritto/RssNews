package ru.itis.rssnews.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.rssnews.dto.page.ArticlesPage;
import ru.itis.rssnews.models.Category;
import ru.itis.rssnews.models.helpers.PageParam;
import ru.itis.rssnews.services.ArticlesService;
import ru.itis.rssnews.services.CategoriesService;

import java.util.Objects;

// TODO: сделать поиск по title
@Controller
@RequiredArgsConstructor
public class MainController {
    private final ArticlesService articlesService;
    private final CategoriesService categoriesService;

    @GetMapping("/")
    public String getMainPage(@RequestParam(value = "page", defaultValue = "1") PageParam pageParam,
                              @RequestParam(value = "isSorted", required = false) String sortParam,
                              @RequestParam(value = "category", required = false) String categoryName,
                              ModelMap modelMap,
                              HttpSession session) {

        setIsSortedToSession(sortParam, session);
        setSelectedCategoryToSession(categoryName, session);

        boolean isSorted = Boolean.parseBoolean(String.valueOf(session.getAttribute("isSorted")));
        Category category = (Category) session.getAttribute("selectedCategory");

        ArticlesPage articlesPage = articlesService.getAll(pageParam.getPage(), isSorted, category);
        modelMap.put("articles", articlesPage.getArticles());
        modelMap.put("pagesCount", articlesPage.getTotalPagesCount());
        modelMap.put("page", pageParam.getPage());
        modelMap.put("isSorted", isSorted);
        modelMap.put("categories", categoriesService.getAll());
        modelMap.put("selectedCategory", category);
        return "index";
    }

    @GetMapping("/search")
    public String getMainPage(@RequestParam(value = "page", defaultValue = "1") PageParam pageParam,
                              @RequestParam(value = "title", required = false) String title,
                              ModelMap modelMap) {

        ArticlesPage articlesPage = articlesService.getAllByTitle(pageParam.getPage(), title);
        modelMap.put("articles", articlesPage.getArticles());
        modelMap.put("pagesCount", articlesPage.getTotalPagesCount());
        modelMap.put("page", pageParam.getPage());
        modelMap.put("searchTitle", title);
        return "found_articles";
    }

    private void setSelectedCategoryToSession(String categoryName, HttpSession session) {
        if (categoryName != null && categoriesService.exists(categoryName)) {
            session.setAttribute("selectedCategory", categoriesService.getByName(categoryName));
        } else if (session.getAttribute("selectedCategory") == null || Objects.equals(categoryName, "all")){
            session.setAttribute("selectedCategory", null);
        }
    }

    private void setIsSortedToSession(String sortParam, HttpSession session) {
        if (sortParam != null) {
            session.setAttribute("isSorted", Boolean.parseBoolean(sortParam));
        } else if (session.getAttribute("isSorted") == null) {
            session.setAttribute("isSorted", false);
        }
    }
}
