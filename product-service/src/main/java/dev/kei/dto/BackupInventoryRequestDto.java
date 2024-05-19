package dev.kei.dto;

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

    public BackupInventoryDto to(BackupInventoryRequestDto backupInventoryRequestDto) {
        return BackupInventoryDto.builder()
                .inventoryId(backupInventoryRequestDto.getInventoryId())
                .productId(backupInventoryRequestDto.getProductId())
                .stock(backupInventoryRequestDto.getStock())
                .build();
    }
}
