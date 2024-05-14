package dev.kei.dto;

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

    public InventoryResponseDto from(InventoryDto inventoryDto) {
        return InventoryResponseDto.builder()
                .id(inventoryDto.getId())
                .productId(inventoryDto.getProductId())
                .stock(inventoryDto.getStock())
                .build();
    }
}
