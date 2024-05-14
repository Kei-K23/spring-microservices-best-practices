package dev.kei.service;

import dev.kei.dto.InventoryRequestDto;
import dev.kei.dto.InventoryResponseDto;
import dev.kei.entity.Inventory;
import dev.kei.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    // create inventory product
    public InventoryResponseDto save(InventoryRequestDto inventoryRequestDto) {
        Inventory inventory = inventoryRequestDto.to(inventoryRequestDto);
        inventoryRepository.save(inventory);

        InventoryResponseDto inventoryResponseDto = new InventoryResponseDto();
        return inventoryResponseDto.from(inventory);
    }

    public List<InventoryResponseDto> findAllInventoryItems() {
        return inventoryRepository.findAll().stream().map(this::mapToInventoryResponse).toList();
    }

    public InventoryResponseDto findInventoryItemByProductId(String productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId);

        InventoryResponseDto inventoryResponseDto = new InventoryResponseDto();
        return inventoryResponseDto.from(inventory);
    }

    private InventoryResponseDto mapToInventoryResponse(Inventory inventory) {
        InventoryResponseDto inventoryResponseDto = new InventoryResponseDto();
        return inventoryResponseDto.from(inventory);
    }

    public InventoryResponseDto update(String productId, InventoryRequestDto inventoryRequestDto) {
        Inventory existingInventory = inventoryRepository.findByProductId(productId);
        existingInventory.setStock(inventoryRequestDto.getStock());

        inventoryRepository.save(existingInventory);

        InventoryResponseDto inventoryResponseDto = new InventoryResponseDto();
        return inventoryResponseDto.from(existingInventory);
    }

    public void delete(String productId) {
        inventoryRepository.deleteByProductId(productId);
    }
}
