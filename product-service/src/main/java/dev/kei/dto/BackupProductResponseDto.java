package dev.kei.dto;

import dev.kei.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BackupProductResponseDto {
    private String id;
    private String productId;
    private String name;
    private String description;
    private Integer price;
    private Integer stock;

    public BackupProductResponseDto from(BackupProductDto backupProductDto) {
        return  BackupProductResponseDto.builder()
                .id(backupProductDto.getId())
                .productId(backupProductDto.getProductId())
                .name(backupProductDto.getName())
                .description(backupProductDto.getDescription())
                .price(backupProductDto.getPrice())
                .stock(backupProductDto.getStock())
                .build();
    }
}
