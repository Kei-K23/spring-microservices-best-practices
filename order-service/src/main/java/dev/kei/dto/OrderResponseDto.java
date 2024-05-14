package dev.kei.dto;

import dev.kei.entity.Order;
import dev.kei.entity.OrderItem;
import dev.kei.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderResponseDto {
    private Long id;
    private String orderCode;
    private String customerId;
    private OrderStatus orderStatus;
    private List<OrderItem> orderItems;

    public OrderResponseDto from(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .orderCode(order.getOrderCode())
                .orderStatus(order.getOrderStatus())
                .orderItems(order.getOrderItems())
                .build();
    }
}
