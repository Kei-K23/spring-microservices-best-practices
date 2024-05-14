package dev.kei.service;

import dev.kei.dto.InventoryRequestDto;
import dev.kei.dto.InventoryResponseDto;
import dev.kei.entity.Inventory;
import dev.kei.repository.InventoryRepository;
import org.springframework.stereotype.Service;

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
}
