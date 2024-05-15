package dev.kei.repository;

import dev.kei.dto.InventoryResponseDto;
import dev.kei.entity.Inventory;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Observed
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByProductId(String productId);

    List<Inventory> findByProductIdIn(List<String> productIdList);
}
