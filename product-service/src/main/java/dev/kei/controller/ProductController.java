package dev.kei.controller;

import dev.kei.dto.ProductRequestDto;
import dev.kei.dto.ProductResponseDto;
import dev.kei.exception.ExceedRateLimitException;
import dev.kei.service.ProductService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/products")
@Slf4j
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @RateLimiter(name = "product-service", fallbackMethod = "saveFallback")
    public ResponseEntity<ProductResponseDto> save(@Valid @RequestBody ProductRequestDto productRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productRequestDto));
    }

    @GetMapping
    @RateLimiter(name = "product-service", fallbackMethod = "findAllProductsFallback")
    public ResponseEntity<List<ProductResponseDto>> findAllProducts() {
        return ResponseEntity.ok().body(productService.findAllProducts());
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "product-service", fallbackMethod = "findProductByIdFallback")
    public ResponseEntity<ProductResponseDto> findProductById(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productService.findProductById(id).get());
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    @RateLimiter(name = "product-service", fallbackMethod = "updateFallback")
    public ResponseEntity<ProductResponseDto> update(@PathVariable String id,@Valid @RequestBody ProductRequestDto productRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productService.update(id, productRequestDto));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "product-service")
    public void delete(@PathVariable String id) {
        try {
            productService.delete(id);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    // Fallback methods
    public ResponseEntity<ProductResponseDto> saveFallback(ProductRequestDto productRequestDto, Exception ex) {
        handleFallback(ex);
        return null;
    }

    public ResponseEntity<List<ProductResponseDto>> findAllProductsFallback(Exception ex) {
        handleFallback(ex);
        return null;
    }

    public ResponseEntity<ProductResponseDto> findProductByIdFallback(String id, Exception ex) {
        handleFallback(ex);
        return null;
    }

    public ResponseEntity<ProductResponseDto> updateFallback(String id, ProductRequestDto productRequestDto, Exception ex) {
        handleFallback(ex);
        return null;
    }

    private void handleFallback(Exception ex) {
        if (ex instanceof NoSuchElementException) {
            log.info("NotFoundException in fallback");
            throw new NoSuchElementException("Product not found");
        } else if(ex instanceof RequestNotPermitted) {
            log.info("RequestNotPermitted in fallback");
            throw new ExceedRateLimitException("You have reached your rate limit. Please try again in 30 seconds.");
        } else {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
