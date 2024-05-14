package dev.kei.service;

import dev.kei.client.InventoryServiceClient;
import dev.kei.dto.InventoryRequestDto;
import dev.kei.dto.ProductRequestDto;
import dev.kei.dto.ProductResponseDto;
import dev.kei.entity.Product;
import dev.kei.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final InventoryServiceClient inventoryServiceClient;

    public ProductService(ProductRepository productRepository, InventoryServiceClient inventoryServiceClient) {
        this.productRepository = productRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    // get all products
    public List<ProductResponseDto> findAllProducts() {
        return productRepository.findAll().stream().map(this::mapToProductResponse).toList();
    }

    // get all products
    public Optional<ProductResponseDto> findProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        ProductResponseDto productResponseDto = new ProductResponseDto();
        return Optional.of(productResponseDto.from(product.get()));
    }

    // create new product
    @Transactional
    public ProductResponseDto save(ProductRequestDto productRequestDto) {
        Product product = Product.builder()
                .name(productRequestDto.getName())
                .description(productRequestDto.getDescription())
                .price(productRequestDto.getPrice())
                .stock(productRequestDto.getStock())
                .build();
        // internal communication with inventory service to create inventory item
        InventoryRequestDto inventoryRequestDto = InventoryRequestDto.builder()
                .productId(product.getId())
                .stock(product.getStock())
                .build();
        inventoryServiceClient.createInventoryItemFromProduct(inventoryRequestDto);
        productRepository.save(product);

        ProductResponseDto productResponseDto = new ProductResponseDto();
        return productResponseDto.from(product);
    }

    // TODO add exception handling
    @Transactional
    public ProductResponseDto update(String id, ProductRequestDto productRequestDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        Product existingProduct = optionalProduct.get();
        existingProduct.setName(productRequestDto.getName());
        existingProduct.setDescription(productRequestDto.getDescription());
        existingProduct.setPrice(productRequestDto.getPrice());
        existingProduct.setStock(productRequestDto.getStock());

        // internal communication with inventory service to update inventory item stock
        InventoryRequestDto inventoryRequestDto = InventoryRequestDto.builder()
                .productId(existingProduct.getId())
                .stock(existingProduct.getStock())
                .build();
        inventoryServiceClient.updateInventoryItemFromProduct(inventoryRequestDto);

        productRepository.save(existingProduct);

        ProductResponseDto productResponseDto = new ProductResponseDto();
        return productResponseDto.from(existingProduct);
    }

    // TODO delete successfully Dto
    @Transactional
    public void delete(String id) {
        // internal communication with inventory service to update inventory item stock
        inventoryServiceClient.deleteInventoryItemFromProduct(id);

        productRepository.deleteById(id);
    }

    // mapping from product to product response
    private ProductResponseDto mapToProductResponse(Product product) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        return productResponseDto.from(product);
    }
}
