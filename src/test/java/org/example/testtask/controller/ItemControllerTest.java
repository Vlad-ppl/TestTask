package org.example.testtask.controller;

import org.example.testtask.entity.ItemEntity;
import org.example.testtask.mapper.ItemMapper;
import org.example.testtask.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc(addFilters = false)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRepository itemRepository;

    @Test
    @DisplayName("GET /items должен возвращать представление с моделью items")
    void testShowItems() throws Exception {
        ItemEntity itemEntity1 = new ItemEntity(1L, "Item 1", 100, Collections.emptyList());
        ItemEntity itemEntity2 = new ItemEntity(2L, "Item 2", 200, Collections.emptyList());
        List<ItemEntity> itemEntities = Arrays.asList(itemEntity1, itemEntity2);

        when(itemRepository.findAll()).thenReturn(itemEntities);
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("items"))
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attribute("items", Arrays.asList(
                        ItemMapper.toDTO(itemEntity1),
                        ItemMapper.toDTO(itemEntity2)
                )));
    }
}
