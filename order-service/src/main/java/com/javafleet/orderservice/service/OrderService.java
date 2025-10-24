package com.javafleet.orderservice.service;
import com.javafleet.orderservice.client.UserClient;
import com.javafleet.orderservice.model.*;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
@Service
public class OrderService {
    private final Faker faker = new Faker(Locale.GERMAN);
    private final List<Order> orders;
    private final UserClient userClient;
    public OrderService(UserClient userClient) {
        this.userClient = userClient;
        this.orders = generateOrders(20);
    }
    public List<Order> getAllOrders() {
        return orders;
    }
    public Order getOrderById(Long id) {
        return orders.stream().filter(o -> o.getId().equals(id)).findFirst().orElse(null);
    }
    public OrderWithUser getOrderWithUser(Long orderId) {
        Order order = getOrderById(orderId);
        if (order == null) return null;
        User user = userClient.getUserById(order.getUserId());
        return new OrderWithUser(order, user);
    }
    private List<Order> generateOrders(int count) {
        List<Order> list = new ArrayList<>();
        String[] statuses = {"PENDING", "PROCESSING", "SHIPPED", "DELIVERED"};
        
        for (int i = 1; i <= count; i++) {
            Order order = new Order();
            order.setId((long) i);
            order.setUserId((long) faker.number().numberBetween(1, 20));
            order.setProduct(faker.commerce().productName());
            order.setQuantity(faker.number().numberBetween(1, 10));
            
            // Fix: Verwende Double statt String, da deutscher Faker "12,99" statt "12.99" liefert
            double priceValue = faker.number().randomDouble(2, 10, 1000);
            order.setPrice(BigDecimal.valueOf(priceValue));
            
            order.setStatus(statuses[faker.number().numberBetween(0, statuses.length)]);
            list.add(order);
        }
        return list;
    }
}
