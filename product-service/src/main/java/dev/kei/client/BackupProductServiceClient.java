package dev.kei.client;

import dev.kei.dto.InventoryRequestDto;
import dev.kei.dto.InventoryResponseDto;
import dev.kei.dto.ProductRequestDto;
import dev.kei.dto.ProductResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class BackupProductServiceClient {
    private final RestTemplate backupRestTemplate;

    public BackupProductServiceClient(RestTemplate backupRestTemplate) {
        this.backupRestTemplate = backupRestTemplate;
    }

    public void createProductForBackupService(ProductRequestDto productRequestDto) {
        log.info("Calling back up service to create product");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<ProductRequestDto> requestEntity = new HttpEntity<>(productRequestDto, headers);
        var response = backupRestTemplate.exchange("/backup/products",
                HttpMethod.POST,
                requestEntity,
                ProductResponseDto.class).getBody();
        log.info("Backup Product service response: {}", response);
    }

    public void updateProductForBackupService(String id, ProductRequestDto productRequestDto) {
        log.info("Calling back up service to update product");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<ProductRequestDto> requestEntity = new HttpEntity<>(productRequestDto, headers);
        var response = backupRestTemplate.exchange("/backup/products/" + id,
                HttpMethod.PUT,
                requestEntity,
                ProductResponseDto.class).getBody();
        log.info("Backup Product service response: {}", response);
    }

    public void deleteProductForBackupService(String id) {
        log.info("Calling back up service to delete product");

        // Create headers if needed
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        var response = backupRestTemplate.exchange(
                "/backup/products/" + id,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        log.info("Backup Product service response: {}", response);
    }
}
