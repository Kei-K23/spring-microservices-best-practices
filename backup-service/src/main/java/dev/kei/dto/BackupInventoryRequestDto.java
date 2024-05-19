package dev.kei.dto;

import dev.kei.entity.Inventory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackupInventoryRequestDto {
    private Long inventoryId;
    @NotBlank
    private String productId;
    @NotNull
    private Integer stock;

    public Inventory to(BackupInventoryRequestDto backupInventoryRequestDto) {
        return Inventory.builder()
                .inventoryId(backupInventoryRequestDto.getInventoryId())
                .productId(backupInventoryRequestDto.getProductId())
                .stock(backupInventoryRequestDto.getStock())
                .build();
    }
}
