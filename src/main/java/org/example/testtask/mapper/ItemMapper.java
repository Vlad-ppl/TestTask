package org.example.testtask.mapper;

import org.example.testtask.dto.Item;
import org.example.testtask.entity.ItemEntity;

public class ItemMapper {

    public static Item toDTO(ItemEntity entity) {
        Item dto = new Item();
        dto.setId(entity.getId());
        dto.setItemName(entity.getName());
        dto.setPrice(entity.getPrice());
        return dto;
    }

    public static ItemEntity toEntity(Item dto) {
        ItemEntity entity = new ItemEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getItemName());
        entity.setPrice(dto.getPrice());
        return entity;
    }
}
