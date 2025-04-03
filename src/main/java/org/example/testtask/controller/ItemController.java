package org.example.testtask.controller;

import lombok.RequiredArgsConstructor;
import org.example.testtask.dto.Item;
import org.example.testtask.mapper.ItemMapper;
import org.example.testtask.repository.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping("/items")
    public String showItems(Model model) {
        List<Item> items = itemRepository.findAll().stream()
                .map(ItemMapper::toDTO)
                .collect(Collectors.toList());
        model.addAttribute("items", items);
        return "items";
    }
}
