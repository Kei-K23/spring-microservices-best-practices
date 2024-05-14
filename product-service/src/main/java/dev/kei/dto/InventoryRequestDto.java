package dev.kei.dto;

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

    public InventoryDto to(InventoryRequestDto inventoryRequestDto) {
        return InventoryDto.builder()
                .productId(inventoryRequestDto.getProductId())
                .stock(inventoryRequestDto.getStock())
                .build();
    }
}
