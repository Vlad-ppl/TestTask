package org.example.testtask.service;

import lombok.RequiredArgsConstructor;
import org.example.testtask.dto.Order;
import org.example.testtask.entity.ItemEntity;
import org.example.testtask.entity.OrderEntity;
import org.example.testtask.entity.UserEntity;
import org.example.testtask.mapper.ItemMapper;
import org.example.testtask.mapper.OrderMapper;
import org.example.testtask.repository.ItemRepository;
import org.example.testtask.repository.OrderRepository;
import org.example.testtask.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public List<Order> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .map(OrderMapper::toDTO)
                .orElse(null);
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    public Order createOrder(Order order, Long userId) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return null;

        List<ItemEntity> items = order.getItems().stream()
                .map(ItemMapper::toEntity)
                .map(item -> itemRepository.findById(item.getId()).orElse(null))
                .filter(i -> i != null)
                .collect(Collectors.toList());

        OrderEntity entity = OrderMapper.toEntity(order, userOpt.get(), items);
        OrderEntity saved = orderRepository.save(entity);
        return OrderMapper.toDTO(saved);
    }

    public void addItemToCurrentOrder(Long userId, Long itemId) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        Optional<ItemEntity> itemOpt = itemRepository.findById(itemId);
        if (userOpt.isEmpty() || itemOpt.isEmpty()) return;

        UserEntity user = userOpt.get();
        ItemEntity item = itemOpt.get();

        OrderEntity newOrder = new OrderEntity();
        newOrder.setUser(user);
        newOrder.setItems(new ArrayList<>(List.of(item)));

        orderRepository.save(newOrder);
    }

    public List<Order> findOrdersByUserId(Long userId) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getUser().getId().equals(userId))
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void removeItemFromLastOrder(Long userId, Long itemId) {
        // Получаем все заказы пользователя по убыванию ID (последние — выше)
        List<OrderEntity> orders = orderRepository.findAll().stream()
                .filter(order -> order.getUser() != null && order.getUser().getId().equals(userId))
                .sorted((o1, o2) -> Long.compare(o2.getOrderId(), o1.getOrderId()))
                .collect(Collectors.toList());

        if (orders.isEmpty()) return;

        OrderEntity lastOrder = orders.get(0); // Последний заказ
        if (lastOrder.getItems() == null) return;

        // Удаляем товар по ID
        lastOrder.getItems().removeIf(item -> item.getId().equals(itemId));

        orderRepository.save(lastOrder);
    }

}
