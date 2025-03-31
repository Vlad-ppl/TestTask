package org.example.testtask.controller;

import org.example.testtask.dto.User;
import org.example.testtask.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("GET /registration должен вернуть страницу регистрации")
    void testRegistrationFormView() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @DisplayName("POST /registration - успешная регистрация редиректит на /login")
    void testSuccessfulRegistration() throws Exception {
        Mockito.when(userService.saveUser(any(User.class))).thenReturn(true);

        mockMvc.perform(post("/registration")
                        .param("name", "John")
                        .param("email", "john@example.com")
                        .param("password", "secret123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("POST /registration - если email уже существует, возвращаем ошибку")
    void testRegistrationWithExistingEmail() throws Exception {
        Mockito.when(userService.saveUser(any(User.class))).thenReturn(false);

        mockMvc.perform(post("/registration")
                        .param("name", "Jane")
                        .param("email", "jane@example.com")
                        .param("password", "secret123"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("errorMessage"));
    }
}
