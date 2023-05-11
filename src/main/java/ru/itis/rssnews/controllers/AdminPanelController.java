package ru.itis.rssnews.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.rssnews.dto.UserDto;
import ru.itis.rssnews.dto.UsersPage;
import ru.itis.rssnews.exceptions.NotFoundException;
import ru.itis.rssnews.exceptions.UpdateEntityException;
import ru.itis.rssnews.models.PageParam;
import ru.itis.rssnews.models.Role;
import ru.itis.rssnews.services.UsersService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adminPanel")
public class AdminPanelController {
    private final UsersService usersService;

    @GetMapping
    public String getAdminPage(@RequestParam(value = "page", defaultValue = "1") PageParam pageParam, ModelMap modelMap) {
        UsersPage usersPage = usersService.getAllUsers(pageParam.getPage());

        System.out.println(usersPage);

        modelMap.put("users", usersPage.getUsers());
        modelMap.put("pagesCount", usersPage.getTotalPagesCount());
        modelMap.put("page", pageParam.getPage());
        return "admin_panel";
    }

    @GetMapping("/users")
    @ResponseBody
    public List<UserDto> getUsers(@RequestParam(value = "query", required = false) String query) {
        query = query.trim();
        String[] splitQuery = query.split(" ");
        String firstName = splitQuery[0];
        String lastName = "";

        if (query.contains(" ")) {
            lastName = splitQuery[splitQuery.length - 1];
        }

        return usersService.getUsersByFirstNameAndLastName(firstName, lastName);
    }

    @PostMapping("/user/{userId}/editRole")
    public String editUserRole(@PathVariable Long userId, @RequestParam Role role, RedirectAttributes attributes) {
        try {
            usersService.updateUserRole(userId, role);
        } catch (NotFoundException | UpdateEntityException ex) {
            attributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/adminPanel";
    }

    @PostMapping("/user/{userId}/delete")
    public String editUserRole(@PathVariable Long userId, RedirectAttributes attributes) {
        try {
            usersService.deleteUser(userId);
        } catch (NotFoundException | UpdateEntityException ex) {
            attributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/adminPanel";
    }
}
