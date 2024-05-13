package dev.kei.controller;

import dev.kei.dto.CreateProductRequest;
import dev.kei.dto.ProductResponse;
import dev.kei.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
