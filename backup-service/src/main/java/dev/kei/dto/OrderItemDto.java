package dev.kei.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderItemDto {
    private Long id;
    private String productId;
    private Integer price;
    private Integer quantity;
}
