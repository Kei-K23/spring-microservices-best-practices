package dev.kei.dto;

import dev.kei.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderStatusUpdateRequestDto {
    @NotNull
    private OrderStatus orderStatus;
}
