package dev.kei.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BackupProductDto {
    private String id;
    private String productId;
    private String name;
    private String description;
    private Integer price;
    private Integer stock;
}
