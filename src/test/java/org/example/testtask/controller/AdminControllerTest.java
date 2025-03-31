package org.example.testtask.controller;

import org.example.testtask.dto.User;
import org.example.testtask.entity.role.UserRole;
import org.example.testtask.service.AdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("GET /admin/users — должен вернуть всех пользователей")
    void testGetAllUsers() throws Exception {
        Mockito.when(adminService.getAllUsers())
                .thenReturn(List.of(new User(1L, "User", "user@example.com", "pass", UserRole.USER)));

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("GET /admin/users?role=USER — должен вернуть пользователей с ролью USER")
    void testFilterByRole() throws Exception {
        Mockito.when(adminService.getUsersByRole(UserRole.USER))
                .thenReturn(List.of(new User(2L, "User2", "u2@example.com", "pass", UserRole.USER)));

        mockMvc.perform(get("/admin/users").param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("GET /admin/users/edit/1 — должен вернуть форму редактирования пользователя")
    void testEditUserForm() throws Exception {
        Mockito.when(adminService.getUserById(1L))
                .thenReturn(new User(1L, "Edit Me", "edit@example.com", "secret", UserRole.ADMIN));

        mockMvc.perform(get("/admin/users/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-user"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("roles"));
    }
}
