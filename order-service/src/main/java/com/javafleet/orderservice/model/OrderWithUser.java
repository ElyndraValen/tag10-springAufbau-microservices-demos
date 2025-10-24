package com.javafleet.orderservice.model;
import lombok.*;
@Data
@AllArgsConstructor
public class OrderWithUser {
    private Order order;
    private User user;
}
