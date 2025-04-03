package org.example.testtask.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.testtask.dto.Item;
import org.example.testtask.dto.Order;
import org.example.testtask.entity.ItemEntity;
import org.example.testtask.entity.UserEntity;
import org.example.testtask.mapper.ItemMapper;
import org.example.testtask.repository.ItemRepository;
import org.example.testtask.repository.UserRepository;
import org.example.testtask.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @GetMapping("/my")
    public String showUserOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(userDetails.getUsername());
        if (userOpt.isEmpty()) return "redirect:/login";

        List<Order> orders = orderService.findOrdersByUserId(userOpt.get().getId());
        model.addAttribute("orders", orders);

        int total = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .mapToInt(Item::getPrice)
                .sum();
        model.addAttribute("total", total);

        return "my-orders";
    }

    @PostMapping("/add-item")
    public String addItemToOrder(@RequestParam("itemId") Long itemId,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 HttpServletRequest request) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(userDetails.getUsername());
        if (userOpt.isEmpty()) return "redirect:/login";

        ItemEntity item = itemRepository.findById(itemId).orElse(null);
        if (item == null) return "redirect:/items";

        Order order = new Order();
        order.setItems(List.of(ItemMapper.toDTO(item)));
        orderService.createOrder(order, userOpt.get().getId());

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/items");
    }

    @PostMapping("/remove-item")
    public String removeItemFromOrder(@RequestParam("itemId") Long itemId,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      HttpServletRequest request) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(userDetails.getUsername());
        if (userOpt.isEmpty()) return "redirect:/login";

        orderService.removeItemFromLastOrder(userOpt.get().getId(), itemId);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/items");
    }

    @PostMapping("/delete")
    public String deleteOrder(@RequestParam("orderId") Long orderId) {
        orderService.deleteOrderById(orderId);
        return "redirect:/orders/my";
    }

}
