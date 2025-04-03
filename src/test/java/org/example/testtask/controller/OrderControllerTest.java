package org.example.testtask.controller;

import org.example.testtask.dto.Item;
import org.example.testtask.dto.Order;
import org.example.testtask.entity.ItemEntity;
import org.example.testtask.entity.UserEntity;
import org.example.testtask.repository.ItemRepository;
import org.example.testtask.repository.UserRepository;
import org.example.testtask.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "test@example.com")
    void testShowUserOrders() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");

        Item item = new Item(1L, "Item 1", 100);
        Order order = new Order();
        order.setItems(List.of(item));

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(orderService.findOrdersByUserId(1L)).thenReturn(List.of(order));

        mockMvc.perform(get("/orders/my"))
                .andExpect(status().isOk())
                .andExpect(view().name("my-orders"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("total", 100));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testAddItemToOrder() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");

        ItemEntity item = new ItemEntity(1L, "Item 1", 100, List.of());

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        mockMvc.perform(post("/orders/add-item")
                        .param("itemId", "1")
                        .header("Referer", "/items"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"));

        verify(orderService, times(1)).createOrder(any(Order.class), eq(1L));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testRemoveItemFromOrder() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/orders/remove-item")
                        .param("itemId", "1")
                        .header("Referer", "/items"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"));

        verify(orderService, times(1)).removeItemFromLastOrder(1L, 1L);
    }

    @Test
    void testDeleteOrder() throws Exception {
        mockMvc.perform(post("/orders/delete")
                        .param("orderId", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/my"));

        verify(orderService, times(1)).deleteOrderById(5L);
    }
}
