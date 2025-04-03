package org.example.testtask.mapper;

import org.example.testtask.dto.Item;
import org.example.testtask.dto.Order;
import org.example.testtask.entity.ItemEntity;
import org.example.testtask.entity.OrderEntity;
import org.example.testtask.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toDTO(OrderEntity entity) {
        Order dto = new Order();
        dto.setId(entity.getOrderId());
        dto.setOrderRole(entity.getOrderRole());

        List<Item> items = entity.getItems().stream()
                .map(ItemMapper::toDTO)
                .collect(Collectors.toList());
        dto.setItems(items);

        return dto;
    }


    public static OrderEntity toEntity(Order dto, UserEntity user, List<ItemEntity> items) {
        OrderEntity entity = new OrderEntity();
        entity.setOrderId(dto.getId());
        entity.setOrderRole(dto.getOrderRole());
        entity.setItems(items);
        entity.setUser(user);
        return entity;
    }
}
