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
public class InventoryRequestDto {
    private String productId;
    private Integer stock;

    public Inventory to(InventoryRequestDto inventoryRequestDto) {
        return Inventory.builder()
                .productId(inventoryRequestDto.getProductId())
                .stock(inventoryRequestDto.getStock())
                .build();
    }
}
