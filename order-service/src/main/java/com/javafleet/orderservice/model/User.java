package com.javafleet.orderservice.model;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String email;
}
