package dev.kei.dto;

import dev.kei.entity.Order;
import dev.kei.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderRequestDto {
    private String customerId;
    private List<OrderItem> orderItems;

    public Order to(OrderRequestDto orderRequestDto) {
        return Order.builder()
                .customerId(orderRequestDto.getCustomerId())
                .orderItems(orderRequestDto.getOrderItems())
                .build();
    }
}
