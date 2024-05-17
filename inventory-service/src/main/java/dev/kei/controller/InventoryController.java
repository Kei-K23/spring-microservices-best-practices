package dev.kei.controller;

import dev.kei.dto.InventoryRequestDto;
import dev.kei.dto.InventoryResponseDto;
import dev.kei.service.InventoryService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/inventory")
@Slf4j
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    @RateLimiter(name = "inventory-service", fallbackMethod = "saveFallback")
    public ResponseEntity<InventoryResponseDto> save(@Valid @RequestBody InventoryRequestDto inventoryRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.save(inventoryRequestDto));
        } catch (Exception ex) {
            if(ex instanceof NoSuchElementException) {
                throw new NoSuchElementException(ex.getMessage());
            } else {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    @GetMapping
    @RateLimiter(name = "inventory-service", fallbackMethod = "findAllInventoryItemsFallback")
    public ResponseEntity<List<InventoryResponseDto>> findAllInventoryItems() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(inventoryService.findAllInventoryItems());
        } catch (Exception ex) {
            if(ex instanceof NoSuchElementException) {
                throw new NoSuchElementException(ex.getMessage());
            } else {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    @GetMapping(params = "productId")
    @RateLimiter(name = "inventory-service", fallbackMethod = "findInventoryItemByIdFallback")
    public ResponseEntity<InventoryResponseDto> findInventoryItemByProductId(@RequestParam(name = "productId") String productId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(inventoryService.findInventoryItemByProductId(productId));
        } catch (Exception ex) {
            if(ex instanceof NoSuchElementException) {
                throw new NoSuchElementException(ex.getMessage());
            } else {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    @GetMapping(value = "/check", params = "productIdList")
    @RateLimiter(name = "inventory-service", fallbackMethod = "checkIsStockEnoughFallback")
    public ResponseEntity<List<InventoryResponseDto>> checkIsStockEnough(@RequestParam(name = "productIdList") List<String> productIdList) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(inventoryService.checkIsStockEnough(productIdList));
        } catch (Exception ex) {
            if(ex instanceof NoSuchElementException) {
                throw new NoSuchElementException(ex.getMessage());
            } else {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    @PutMapping(params = "productId")
    @RateLimiter(name = "inventory-service", fallbackMethod = "updateFallback")
    public ResponseEntity<InventoryResponseDto> update(@RequestParam(name = "productId") String productId,@Valid @RequestBody InventoryRequestDto inventoryRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(inventoryService.update(productId, inventoryRequestDto));
        } catch (Exception ex) {
            if(ex instanceof NoSuchElementException) {
                throw new NoSuchElementException(ex.getMessage());
            } else {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    @DeleteMapping(params = "productId")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RateLimiter(name = "inventory-service")
    public void delete(@RequestParam(name = "productId") String productId) {
        inventoryService.delete(productId);
    }

    // Fallback methods
    public ResponseEntity<InventoryResponseDto> saveFallback(InventoryRequestDto inventoryRequestDto, Exception ex) {
        handleFallback(ex);
        return null;
    }

    public ResponseEntity<List<InventoryResponseDto>> findAllInventoryItemsFallback(Exception ex) {
        handleFallback(ex);
        return null;
    }

    public ResponseEntity<List<InventoryResponseDto>> checkIsStockEnoughFallback(List<String> productIdList, Exception ex) {
        handleFallback(ex);
        return null;
    }

    public ResponseEntity<InventoryResponseDto> findInventoryItemByIdFallback(String id, Exception ex) {
        handleFallback(ex);
        return null;
    }

    public ResponseEntity<InventoryResponseDto> updateFallback(String id, InventoryRequestDto inventoryRequestDto, Exception ex) {
        handleFallback(ex);
        return null;
    }

    private void handleFallback(Exception ex) {
        if (ex instanceof NoSuchElementException) {
            log.info("NotFoundException in fallback");
            throw new NoSuchElementException("Inventory product item not found");
        } else {
            log.info("Rate limit exceeded");
            throw new RuntimeException("You have reached your rate limit. Please try again in 30 seconds.");
        }
    }
}
