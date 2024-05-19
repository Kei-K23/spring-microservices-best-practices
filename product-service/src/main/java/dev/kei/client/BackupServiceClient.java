package dev.kei.client;

import dev.kei.dto.InventoryRequestDto;
import dev.kei.dto.InventoryResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class BackupServiceClient {
    private final RestTemplate backupRestTemplate;

    public BackupServiceClient(RestTemplate backupRestTemplate) {
        this.backupRestTemplate = backupRestTemplate;
    }

    public void createProductForBackupService(InventoryRequestDto inventoryRequestDto) {
        log.info("Calling back up service to create product");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<InventoryRequestDto> requestEntity = new HttpEntity<>(inventoryRequestDto, headers);
        var response = backupRestTemplate.exchange("/backup/products",
                HttpMethod.POST,
                requestEntity,
                InventoryResponseDto.class).getBody();
        log.info("Inventory service response: {}", response);
    }

    public void updateProductForBackupService(InventoryRequestDto inventoryRequestDto) {
        log.info("Calling back up service to update product");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<InventoryRequestDto> requestEntity = new HttpEntity<>(inventoryRequestDto, headers);
        var response = backupRestTemplate.exchange("/backup/products?productId=" + inventoryRequestDto.getProductId(),
                HttpMethod.PUT,
                requestEntity,
                InventoryResponseDto.class).getBody();
        log.info("Inventory service response: {}", response);
    }

    public void deleteProductForBackupService(String productId) {
        log.info("Calling back up service to delete product");

        // Create headers if needed
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        var response = backupRestTemplate.exchange(
                "/backup/products?productId=" + productId,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        log.info("Inventory service response: {}", response);
    }
}
