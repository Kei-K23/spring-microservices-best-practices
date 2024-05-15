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

    @Transactional(readOnly = true)
    public List<OrderResponseDto> findAllOrders() {
        return orderRepository.findAll().stream().map(this::mapToOrderResponse).toList();
    }

    @Transactional(readOnly = true)
    public OrderResponseDto findOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        return orderResponseDto.from(order.get());
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> findOrdersByCustomerId(String customerId) {
        return orderRepository.findAllByCustomerId(customerId).stream().map(this::mapToOrderResponse).toList();
    }

    @Transactional
    public OrderResponseDto update(Long id, OrderRequestDto orderRequestDto) {
        // TODO handle not found order
        Order order = orderRepository.findById(id).get();
        order.setOrderItems(orderRequestDto.getOrderItems());
        order.setOrderStatus(orderRequestDto.getOrderStatus());
        orderRepository.save(order);

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        return orderResponseDto.from(order);
    }

    @Transactional
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    private OrderResponseDto mapToOrderResponse(Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        return orderResponseDto.from(order);
    }
}
