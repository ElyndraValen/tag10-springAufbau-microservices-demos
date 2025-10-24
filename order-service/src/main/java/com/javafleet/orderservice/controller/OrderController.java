package com.javafleet.orderservice.controller;
import com.javafleet.orderservice.model.*;
import com.javafleet.orderservice.service.OrderService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
    @GetMapping("/{id}/with-user")
    public OrderWithUser getOrderWithUser(@PathVariable Long id) {
        return orderService.getOrderWithUser(id);
    }
}
