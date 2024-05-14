package dev.kei.service;

import dev.kei.dto.OrderRequestDto;
import dev.kei.dto.OrderResponseDto;
import dev.kei.entity.Order;
import dev.kei.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponseDto save(OrderRequestDto orderRequestDto) {
        Order order = orderRequestDto.to(orderRequestDto);
        order.setOrderCode(UUID.randomUUID().toString());
        orderRepository.save(order);

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        return orderResponseDto.from(order);
    }
}
