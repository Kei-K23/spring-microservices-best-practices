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
public class InventoryRequestDto {
    @NotBlank
    private String productId;
    @NotNull
    private Integer stock;

    public InventoryDto to(InventoryRequestDto inventoryRequestDto) {
        return InventoryDto.builder()
                .productId(inventoryRequestDto.getProductId())
                .stock(inventoryRequestDto.getStock())
                .build();
    }
}
