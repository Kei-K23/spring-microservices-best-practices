package dev.kei.dto;

import dev.kei.entity.Order;
import dev.kei.entity.OrderItem;
import dev.kei.entity.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    private String customerId;
    @NotNull
    private OrderStatus orderStatus;
    @NotNull
    private List<OrderItem> orderItems;

    public Order to(OrderRequestDto orderRequestDto) {
        return Order.builder()
                .customerId(orderRequestDto.getCustomerId())
                .orderStatus(orderRequestDto.getOrderStatus())
                .orderItems(orderRequestDto.getOrderItems())
                .build();
    }
}
