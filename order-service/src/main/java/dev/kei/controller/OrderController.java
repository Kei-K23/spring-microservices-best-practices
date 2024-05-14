package dev.kei.controller;

import dev.kei.dto.OrderRequestDto;
import dev.kei.dto.OrderResponseDto;
import dev.kei.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto save(@RequestBody OrderRequestDto orderRequestDto) {
        return orderService.save(orderRequestDto);
    }
}
