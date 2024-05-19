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

    public BackupInventoryResponseDto from(BackupInventoryDto backupInventoryDto) {
        return BackupInventoryResponseDto.builder()
                .id(backupInventoryDto.getId())
                .inventoryId(backupInventoryDto.getInventoryId())
                .productId(backupInventoryDto.getProductId())
                .stock(backupInventoryDto.getStock())
                .build();
    }
}
