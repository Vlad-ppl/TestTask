package org.example.testtask.controller;

import lombok.RequiredArgsConstructor;
import org.example.testtask.dto.User;
import org.example.testtask.entity.role.UserRole;
import org.example.testtask.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

//    @GetMapping("/admin/users")
//    public String allUsers(Model model) {
//        List<User> users = adminService.getAllUsers();
//        model.addAttribute("users", users);
//        return "users"; // thymeleaf-шаблон users.html
//    }

    @GetMapping("/admin/users/filter")
    public String filterByRole(@RequestParam("role") String role, Model model) {
        List<User> users = adminService.getUsersByRole(UserRole.valueOf(role));
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String confirmDeleteUser(@PathVariable Long id, Model model) {
        User user = adminService.getUserById(id);
        model.addAttribute("user", user);
        return "confirm-delete-user";
    }

    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = adminService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", UserRole.values());
        return "edit-user";
    }

    @PostMapping("/admin/users/edit")
    public String updateUser(@ModelAttribute("user") User user) {
        adminService.updateUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users")
    public String searchAndFilterUsers(@RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) String role,
                                       Model model) {
        List<User> users;

        if ((keyword == null || keyword.isBlank()) && (role == null || role.isBlank())) {
            users = adminService.getAllUsers();
        } else if (role != null && !role.isBlank()) {
            users = adminService.getUsersByRole(UserRole.valueOf(role));
            if (keyword != null && !keyword.isBlank()) {
                String lowerKeyword = keyword.toLowerCase();
                users = users.stream()
                        .filter(user -> user.getName().toLowerCase().contains(lowerKeyword)
                                || user.getEmail().toLowerCase().contains(lowerKeyword))
                        .toList();
            }
        } else {
            String lowerKeyword = keyword.toLowerCase();
            users = adminService.getAllUsers().stream()
                    .filter(user -> user.getName().toLowerCase().contains(lowerKeyword)
                            || user.getEmail().toLowerCase().contains(lowerKeyword))
                    .toList();
        }

        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedRole", role);
        return "users";
    }

}
