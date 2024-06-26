package dev.kei.service;

import dev.kei.client.BackupInventoryServiceClient;
import dev.kei.client.BackupProductServiceClient;
import dev.kei.client.InventoryServiceClient;
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
    private final InventoryServiceClient inventoryServiceClient;
    private final BackupInventoryServiceClient backupInventoryServiceClient;
    private final BackupProductServiceClient backupProductServiceClient;

    public ProductService(ProductRepository productRepository,
                          InventoryServiceClient inventoryServiceClient,
                          BackupInventoryServiceClient backupInventoryServiceClient,
                          BackupProductServiceClient backupProductServiceClient) {
        this.productRepository = productRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.backupInventoryServiceClient = backupInventoryServiceClient;
        this.backupProductServiceClient = backupProductServiceClient;
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
    public ProductResponseDto save(ProductRequestDto productRequestDto) {
        Product product = Product.builder()
                .name(productRequestDto.getName())
                .description(productRequestDto.getDescription())
                .price(productRequestDto.getPrice())
                .stock(productRequestDto.getStock())
                .build();
        Product createdProduct = productRepository.save(product);

        try {
            // internal communication with inventory service to create inventory item
            InventoryRequestDto inventoryRequestDto = InventoryRequestDto.builder()
                    .productId(product.getId())
                    .stock(product.getStock())
                    .build();


            Long inventoryId =inventoryServiceClient.createInventoryItemFromProduct(inventoryRequestDto);
            BackupInventoryRequestDto backupInventoryRequestDto = BackupInventoryRequestDto.builder()
                    .inventoryId(inventoryId)
                    .productId(product.getId())
                    .stock(product.getStock())
                    .build();

            BackupProductRequestDto backupProductRequestDto = BackupProductRequestDto.builder()
                    .productId(createdProduct.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .stock(product.getStock())
                    .build();

            backupInventoryServiceClient.createInventoryItemForBackupService(backupInventoryRequestDto);
            backupProductServiceClient.createProductForBackupService(backupProductRequestDto);
        } catch (Exception ex) {
            System.out.println("ERROR::::" + ex);
            // If there is an exception, rollback the transaction
            throw new RuntimeException("Failed to create inventory item, rolling back transaction", ex);
        }

        ProductResponseDto productResponseDto = new ProductResponseDto();
        return productResponseDto.from(product);
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
            inventoryServiceClient.updateInventoryItemFromProduct(inventoryRequestDto);
            backupInventoryServiceClient.updateInventoryItemForBackupService(inventoryRequestDto);
            backupProductServiceClient.updateProductForBackupService(id, productRequestDto);

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
        try {
            // internal communication with inventory service to delete inventory item stock
            inventoryServiceClient.deleteInventoryItemFromProduct(id);
            backupInventoryServiceClient.deleteInventoryItemForBackupService(id);
            backupProductServiceClient.deleteProductForBackupService(id);

            productRepository.deleteById(id);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
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
