package org.example.testtask.controller;

import lombok.RequiredArgsConstructor;
import org.example.testtask.entity.UserEntity;
import org.example.testtask.entity.role.UserRole;
import org.example.testtask.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/admin/users")
    public String allUsers(Model model) {
        List<UserEntity> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "users"; // thymeleaf-шаблон users.html
    }

    @GetMapping("/admin/users/filter")
    public String filterByRole(@RequestParam("role") String role, Model model) {
        List<UserEntity> users = adminService.getUsersByRole(UserRole.valueOf(role));
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String confirmDeleteUser(@PathVariable Long id, Model model) {
        UserEntity user = adminService.getUserById(id);
        model.addAttribute("user", user);
        return "confirm-delete-user";  // Шаблон подтверждения удаления
    }

    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return "redirect:/admin/users";  // Перенаправляем на список пользователей после удаления
    }

    @GetMapping("/admin/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        UserEntity user = adminService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", UserRole.values());  // Добавляем список ролей для выбора
        return "edit-user";  // Шаблон редактирования
    }

    @PostMapping("/admin/users/edit")
    public String updateUser(UserEntity user) {
        adminService.updateUser(user);
        return "redirect:/admin/users";  // Перенаправляем обратно на список пользователей
    }
}
