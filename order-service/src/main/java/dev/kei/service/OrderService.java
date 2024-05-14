package dev.kei.service;

import dev.kei.dto.OrderRequestDto;
import dev.kei.dto.OrderResponseDto;
import dev.kei.entity.Order;
import dev.kei.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    public List<OrderResponseDto> findAllOrders() {
        return orderRepository.findAll().stream().map(this::mapToOrderResponse).toList();
    }

    public OrderResponseDto findOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        return orderResponseDto.from(order.get());
    }

    public List<OrderResponseDto> findOrdersByCustomerId(String customerId) {
        return orderRepository.findAllByCustomerId(customerId).stream().map(this::mapToOrderResponse).toList();
    }

    private OrderResponseDto mapToOrderResponse(Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        return orderResponseDto.from(order);
    }
}
