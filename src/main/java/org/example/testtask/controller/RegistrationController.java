package org.example.testtask.controller;

import lombok.AllArgsConstructor;
import org.example.testtask.dto.User;
import org.example.testtask.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") User user, Model model) {
        boolean success = userService.saveUser(user);
        if (!success) {
            model.addAttribute("errorMessage", "Пользователь с таким email уже существует!");
            return "registration"; // остаёмся на странице регистрации
        }
        return "redirect:/login";
    }
}
