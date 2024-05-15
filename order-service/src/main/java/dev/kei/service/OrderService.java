package dev.kei.service;

import dev.kei.client.InventoryServiceClient;
import dev.kei.dto.InventoryResponseDto;
import dev.kei.dto.OrderRequestDto;
import dev.kei.dto.OrderResponseDto;
import dev.kei.dto.OrderStatusUpdateRequestDto;
import dev.kei.entity.Order;
import dev.kei.entity.OrderItem;
import dev.kei.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryServiceClient inventoryServiceClient;

    public OrderService(OrderRepository orderRepository, InventoryServiceClient inventoryServiceClient) {
        this.orderRepository = orderRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @Transactional
    public OrderResponseDto save(OrderRequestDto orderRequestDto) {
        Order order = orderRequestDto.to(orderRequestDto);
        order.setOrderCode(UUID.randomUUID().toString());
        orderRepository.save(order);

        List<OrderItem> orderItems = order.getOrderItems();
        List<String> productIdList = orderItems.stream().map(OrderItem::getProductId).toList();

        try {
            // call inventory service to check products are in stock
            List<InventoryResponseDto> inventoryResponseDtos = inventoryServiceClient.checkInventoryForStockIsEnough(productIdList);
            if (!inventoryResponseDtos.stream().allMatch(inventoryResponseDto -> isStockEnough(orderItems, inventoryResponseDto))) {
                throw new RuntimeException("Failed to place order! No enough stock, rolling back transaction");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to place order, rolling back transaction", ex);
        }

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
    public OrderResponseDto orderStatusUpdate(Long id, OrderStatusUpdateRequestDto orderStatusUpdateRequestDto) {
        // TODO handle not found order
        Order order = orderRepository.findById(id).get();
        order.setOrderStatus(orderStatusUpdateRequestDto.getOrderStatus());
        orderRepository.save(order);

        // Todo make messaging communication to update stock of inventory and product when order confirm
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

    // check order items is in stock
    private Boolean isStockEnough(List<OrderItem> orderItems, InventoryResponseDto inventoryResponseDto) {
        return orderItems.stream().
                filter(orderItem -> orderItem.getProductId().equals(inventoryResponseDto.getProductId()))
                .allMatch(orderItem -> inventoryResponseDto.getStock() >= orderItem.getQuantity());
    }
}
