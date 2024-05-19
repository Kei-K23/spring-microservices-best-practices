package dev.kei.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BackupProductRequestDto {
    private String productId;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private Integer price;
    @NotNull
    private Integer stock;
}
