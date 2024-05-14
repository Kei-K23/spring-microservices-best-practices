package dev.kei.controller;

import dev.kei.dto.OrderRequestDto;
import dev.kei.dto.OrderResponseDto;
import dev.kei.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponseDto> findAllOrders() {
        return orderService.findAllOrders();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponseDto findOrderById(@PathVariable(name = "id") Long id) {
        return orderService.findOrderById(id);
    }

    @GetMapping(params = "customerId")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponseDto> findOrdersByCustomerId(@RequestParam(name = "customerId") String customerId) {
        return orderService.findOrdersByCustomerId(customerId);
    }
}
