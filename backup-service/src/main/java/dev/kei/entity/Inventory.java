package dev.kei.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "inventory")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Inventory {
    @Id
    private String id;
    @Indexed(unique = true)
    private Long inventoryId;
    @Indexed(unique = true)
    private String productId;
    private Integer stock;
}
