package org.example.testtask.controller;

import lombok.AllArgsConstructor;
import org.example.testtask.entity.UserEntity;
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
        model.addAttribute("userEntity", new UserEntity());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userEntity") UserEntity userEntity, Model model) {
        boolean success = userService.saveUser(userEntity);
        if (!success) {
            model.addAttribute("errorMessage", "Пользователь с таким email уже существует!");
            return "registration"; // остаёмся на странице регистрации
        }
        return "redirect:/login";
    }
}
