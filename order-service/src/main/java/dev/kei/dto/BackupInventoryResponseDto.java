package dev.kei.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackupInventoryResponseDto {
    private String id;
    private Long inventoryId;
    private String productId;
    private Integer stock;

    public BackupInventoryResponseDto from(BackupInventoryDto inventory) {
        return BackupInventoryResponseDto.builder()
                .id(inventory.getId())
                .inventoryId(inventory.getInventoryId())
                .productId(inventory.getProductId())
                .stock(inventory.getStock())
                .build();
    }
}
