package com.javafleet.orderservice.model;
import lombok.*;
import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private Long userId;
    private String product;
    private Integer quantity;
    private BigDecimal price;
    private String status;
}
