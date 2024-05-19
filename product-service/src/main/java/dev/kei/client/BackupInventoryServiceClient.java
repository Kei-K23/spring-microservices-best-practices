package dev.kei.client;

import dev.kei.dto.BackupInventoryRequestDto;
import dev.kei.dto.BackupInventoryResponseDto;
import dev.kei.dto.InventoryRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class BackupInventoryServiceClient {
    private final RestTemplate backupRestTemplate;

    public BackupInventoryServiceClient(RestTemplate backupRestTemplate) {
        this.backupRestTemplate = backupRestTemplate;
    }

    public void createInventoryItemForBackupService(BackupInventoryRequestDto backupInventoryRequestDto) {
        log.info("Calling back up service to create inventory item");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<BackupInventoryRequestDto> requestEntity = new HttpEntity<>(backupInventoryRequestDto, headers);
        var response = backupRestTemplate.exchange("/backup/inventory",
                HttpMethod.POST,
                requestEntity,
                BackupInventoryResponseDto.class).getBody();
        log.info("Backup Inventory service response: {}", response);
    }

    public void updateInventoryItemForBackupService(InventoryRequestDto inventoryRequestDto) {
        log.info("Calling back up service to update inventory item");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<InventoryRequestDto> requestEntity = new HttpEntity<>(inventoryRequestDto, headers);
        var response = backupRestTemplate.exchange("/backup/inventory?productId=" + inventoryRequestDto.getProductId(),
                HttpMethod.PUT,
                requestEntity,
                BackupInventoryResponseDto.class).getBody();
        log.info("Backup Inventory service response: {}", response);
    }

    public void deleteInventoryItemForBackupService(String productId) {
        log.info("Calling back up service to delete inventory item");

        // Create headers if needed
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        var response = backupRestTemplate.exchange(
                "/backup/inventory?productId=" + productId,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        log.info("Backup Inventory service response: {}", response);
    }
}
