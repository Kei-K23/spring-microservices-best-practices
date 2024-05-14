package dev.kei.repository;


import dev.kei.dto.InventoryResponseDto;
import dev.kei.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByProductId(String productId);

    void deleteByProductId(String productId);
}
