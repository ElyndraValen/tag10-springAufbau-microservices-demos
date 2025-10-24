package com.javafleet.orderservice.client;
import com.javafleet.orderservice.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    User getUserById(@PathVariable Long id);
}
