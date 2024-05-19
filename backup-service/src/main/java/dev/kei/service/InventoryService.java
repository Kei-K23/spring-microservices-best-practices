package dev.kei.service;

import dev.kei.dto.BackupInventoryRequestDto;
import dev.kei.dto.BackupInventoryResponseDto;
import dev.kei.dto.InventoryRequestDto;
import dev.kei.dto.InventoryResponseDto;
import dev.kei.entity.Inventory;
import dev.kei.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {
    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    // create inventory product
    public BackupInventoryResponseDto save(BackupInventoryRequestDto backupInventoryRequestDto) {
        Inventory inventory = backupInventoryRequestDto.to(backupInventoryRequestDto);
        inventoryRepository.save(inventory);

        BackupInventoryResponseDto backupInventoryResponseDto = new BackupInventoryResponseDto();
        return backupInventoryResponseDto.from(inventory);
    }

    @Transactional(readOnly = true)
    public List<InventoryResponseDto> findAllInventoryItems() {
        return inventoryRepository.findAll().stream().map(this::mapToInventoryResponse).toList();
    }

    @Transactional(readOnly = true)
    public InventoryResponseDto findInventoryItemByProductId(String productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId);

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

    @Transactional(readOnly = true)
    public List<InventoryResponseDto> checkIsStockEnough(List<String> productIdList) {
        log.info("Checking inventory");
        return inventoryRepository.findByProductIdIn(productIdList).stream()
                .map(inventory -> {
                    InventoryResponseDto inventoryResponseDto = new InventoryResponseDto();
                    return inventoryResponseDto.from(inventory);
                }).filter(inventoryResponseDto -> inventoryResponseDto.getStock() > 0).toList();
    }

    @Transactional
    public void updateInventoryItemStock(String productId, Integer stock) {
        try {
            Inventory existingInventory = inventoryRepository.findByProductId(productId);
            if(existingInventory.getStock() >= stock) {
                existingInventory.setStock(existingInventory.getStock() - stock);
                inventoryRepository.save(existingInventory);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error when updating inventory item stock: ", ex);
        }
    }

    public void delete(String productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId);
        inventoryRepository.delete(inventory);
    }

    private InventoryResponseDto mapToInventoryResponse(Inventory inventory) {
        InventoryResponseDto inventoryResponseDto = new InventoryResponseDto();
        return inventoryResponseDto.from(inventory);
    }
}
