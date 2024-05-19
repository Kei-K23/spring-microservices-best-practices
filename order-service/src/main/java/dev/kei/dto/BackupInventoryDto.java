package dev.kei.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BackupInventoryDto {
    private String id;
    private Long inventoryId;
    private String productId;
    private Integer stock;
}
