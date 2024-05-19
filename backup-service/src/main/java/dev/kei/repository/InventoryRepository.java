package dev.kei.repository;

import dev.kei.entity.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends MongoRepository<Inventory, String> {
    Inventory findByProductId(String productId);

    List<Inventory> findByProductIdIn(List<String> productIdList);
}
