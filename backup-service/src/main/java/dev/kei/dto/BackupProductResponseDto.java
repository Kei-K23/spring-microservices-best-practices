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

    public BackupProductResponseDto from(Product product) {
        return  BackupProductResponseDto.builder()
                .id(product.getId())
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }
}
