package org.example.testtask.controller;

import org.example.testtask.dto.User;
import org.example.testtask.entity.role.UserRole;
import org.example.testtask.service.ProfileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @Test
    @DisplayName("GET / — должен вернуть страницу профиля для залогиненного пользователя")
    @WithMockUser(username = "test@example.com")
    void testHome() throws Exception {
        Mockito.when(profileService.getUserByEmail(anyString()))
                .thenReturn(new User(1L, "Test User", "test@example.com", "secret", UserRole.USER));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @DisplayName("GET /profile/edit — должен вернуть форму редактирования профиля")
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    void testEditProfile() throws Exception {
        Mockito.when(profileService.getUserByEmail(anyString()))
                .thenReturn(new User(1L, "Test User", "test@example.com", "secret", UserRole.USER));

        mockMvc.perform(get("/profile/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-profile"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @DisplayName("POST /profile/edit — должен обновить профиль и редиректить на /")
    @WithMockUser(username = "test@example.com")
    void testUpdateProfile() throws Exception {
        mockMvc.perform(post("/profile/edit")
                        .with(csrf()) // 🔥 добавляет CSRF токен
                        .param("id", "1")
                        .param("name", "Updated User")
                        .param("email", "test@example.com")
                        .param("password", "")
                        .param("userRole", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
