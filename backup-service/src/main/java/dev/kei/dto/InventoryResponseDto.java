package dev.kei.dto;

import dev.kei.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponseDto {
    private Long id;
    private String productId;
    private Integer stock;

    public InventoryResponseDto from(Inventory inventory) {
        return InventoryResponseDto.builder()
                .id(inventory.getId())
                .productId(inventory.getProductId())
                .stock(inventory.getStock())
                .build();
    }
}
