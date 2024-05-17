package dev.kei.controller;

import dev.kei.dto.CustomErrorResponseDto;
import dev.kei.dto.ProductRequestDto;
import dev.kei.dto.ProductResponseDto;
import dev.kei.service.ProductService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    @ResponseStatus(HttpStatus.OK)
    @RateLimiter(name = "product-service", fallbackMethod = "findAllProductsFallback")
    public ResponseEntity<List<ProductResponseDto>> findAllProducts() {
        return ResponseEntity.ok().body(productService.findAllProducts());
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "product-service", fallbackMethod = "findProductByIdFallback")
    public ResponseEntity<Optional<ProductResponseDto>> findProductById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findProductById(id));
    }

    @PutMapping("/{id}")
    @RateLimiter(name = "product-service", fallbackMethod = "updateFallback")
    public ResponseEntity<ProductResponseDto> update(@PathVariable String id,@Valid @RequestBody ProductRequestDto productRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.update(id, productRequestDto));
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "product-service")
    public void delete(@PathVariable String id) {
        productService.delete(id);
    }

    // Fallback methods
    public ResponseEntity<ProductResponseDto> saveFallback(ProductRequestDto productRequestDto, Throwable throwable) {
        throw new RuntimeException();
    }

    public ResponseEntity<List<ProductResponseDto>> findAllProductsFallback(Throwable throwable) {
        log.info("Call fallback method for rate limiting method");
        throw new RuntimeException("You have reached your rate limit. Please try again in 30 seconds.");
    }

    public CustomErrorResponseDto findProductByIdFallback(String id, Throwable throwable) {
        return createFallbackResponse();
    }

    public CustomErrorResponseDto updateFallback(String id, ProductRequestDto productRequestDto, Throwable throwable) {
        return createFallbackResponse();
    }

    private CustomErrorResponseDto createFallbackResponse() {
        log.info("Calling fallback method for rate limit");
        return CustomErrorResponseDto.builder()
                .code(HttpStatus.TOO_MANY_REQUESTS.value())
                .message("You have reached your rate limit. Please try again in 60 seconds.")
                .build();
    }
}
