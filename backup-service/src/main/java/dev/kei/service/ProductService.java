package dev.kei.service;

import dev.kei.dto.*;
import dev.kei.entity.Product;
import dev.kei.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // get all products
    public List<ProductResponseDto> findAllProducts() {
        return productRepository.findAll().stream().map(this::mapToProductResponse).toList();
    }

    // get all products
    public Optional<ProductResponseDto> findProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()) {
            throw new NoSuchElementException("Product with id " + id + " not found");
        }
        
        ProductResponseDto productResponseDto = new ProductResponseDto();
        return Optional.of(productResponseDto.from(product.get()));
    }

    // create new product
    @Transactional
    public BackupProductResponseDto save(BackupProductRequestDto backupProductRequestDto) {
        Product product = Product.builder()
                .productId(backupProductRequestDto.getProductId())
                .name(backupProductRequestDto.getName())
                .description(backupProductRequestDto.getDescription())
                .price(backupProductRequestDto.getPrice())
                .stock(backupProductRequestDto.getStock())
                .build();
        productRepository.save(product);

        BackupProductResponseDto backupProductResponseDto = new BackupProductResponseDto();
        return backupProductResponseDto.from(product);
    }

    @Transactional
    public ProductResponseDto update(String id, ProductRequestDto productRequestDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty()) {
            throw new NoSuchElementException("Product with id " + id + " not found to delete");
        }
        try {
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
            productRepository.save(existingProduct);
            ProductResponseDto productResponseDto = new ProductResponseDto();
            return productResponseDto.from(existingProduct);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void delete(String id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NoSuchElementException("Product with id " + id + " not found to delete");
        }

        productRepository.deleteById(id);
    }

    @Transactional
    public void updateStock(String id, Integer stock) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(id);
            Product existingProduct = optionalProduct.get();

            if(existingProduct.getStock() >= stock) {
                // update the stock
                existingProduct.setStock(existingProduct.getStock() - stock);
                productRepository.save(existingProduct);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error when updating product stock: ", ex);
        }
    }

    // mapping from product to product response
    private ProductResponseDto mapToProductResponse(Product product) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        return productResponseDto.from(product);
    }
}
