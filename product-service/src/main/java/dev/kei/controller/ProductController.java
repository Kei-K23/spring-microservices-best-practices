package dev.kei.controller;

import dev.kei.dto.CreateProductRequest;
import dev.kei.dto.ProductResponse;
import dev.kei.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse save(@RequestBody CreateProductRequest createProductRequest) {
        return productService.save(createProductRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> findAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<ProductResponse> findProductById(@PathVariable String id) {
        return productService.findProductById(id);
    }
}
