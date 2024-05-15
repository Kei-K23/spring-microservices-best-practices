package dev.kei.controller;

import dev.kei.dto.InventoryRequestDto;
import dev.kei.dto.InventoryResponseDto;
import dev.kei.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponseDto save(@Valid @RequestBody InventoryRequestDto inventoryRequestDto) {
        return inventoryService.save(inventoryRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponseDto> findAllInventoryItems() {
        return inventoryService.findAllInventoryItems();
    }

    @GetMapping(params = "productId")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponseDto findInventoryItemByProductId(@RequestParam(name = "productId") String productId) {
        return inventoryService.findInventoryItemByProductId(productId);
    }

    @GetMapping(value = "/check", params = "productIdList")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponseDto> checkIsStockEnough(@RequestParam(name = "productIdList") List<String> productIdList) {
        return inventoryService.checkIsStockEnough(productIdList);
    }

    @PutMapping(params = "productId")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponseDto update(@RequestParam(name = "productId") String productId,@Valid @RequestBody InventoryRequestDto inventoryRequestDto) {
        return inventoryService.update(productId, inventoryRequestDto);
    }

    @DeleteMapping(params = "productId")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam(name = "productId") String productId) {
        inventoryService.delete(productId);
    }
}
