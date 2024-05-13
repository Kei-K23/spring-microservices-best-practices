package dev.kei.service;

import dev.kei.dto.CreateProductRequest;
import dev.kei.dto.ProductResponse;
import dev.kei.entity.Product;
import dev.kei.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // get all products
    public List<ProductResponse> findAllProducts() {
        return productRepository.findAll().stream().map(this::mapToProductResponse).toList();
    }

    // get all products
    public Optional<ProductResponse> findProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        ProductResponse productResponse = new ProductResponse();
        return Optional.of(productResponse.from(product.get()));
    }

    // create new product
    public ProductResponse save(CreateProductRequest createProductRequest) {
        Product product = Product.builder()
                .name(createProductRequest.getName())
                .description(createProductRequest.getDescription())
                .price(createProductRequest.getPrice())
                .stock(createProductRequest.getStock())
                .build();
        productRepository.save(product);
        ProductResponse productResponse = new ProductResponse();
        return productResponse.from(product);
    }

    // mapping from product to product response
    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        return productResponse.from(product);
    }
}
