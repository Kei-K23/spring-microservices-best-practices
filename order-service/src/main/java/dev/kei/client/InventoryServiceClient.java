package dev.kei.client;

import dev.kei.dto.BackupInventoryResponseDto;
import dev.kei.dto.InventoryResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class InventoryServiceClient {
    private final RestTemplate inventoryRestTemplate;
    private final RestTemplate backupInventoryRestTemplate;

    public InventoryServiceClient(RestTemplate inventoryRestTemplate, RestTemplate backupInventoryRestTemplate) {
        this.inventoryRestTemplate = inventoryRestTemplate;
        this.backupInventoryRestTemplate = backupInventoryRestTemplate;
    }

    public List<InventoryResponseDto> checkInventoryForStockIsEnough(List<String> productIdList) {
        log.info("Calling inventory service to check products is in stock");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String url = "/inventory/check?productIdList=" + String.join(",", productIdList);

        ResponseEntity<List<InventoryResponseDto>> response = inventoryRestTemplate.exchange(url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<InventoryResponseDto>>() {});
        log.info("Inventory service response: {}", response);
        return response.getBody();
    }

    public List<BackupInventoryResponseDto> checkBackupInventoryForStockIsEnough(List<String> productIdList) {
        log.info("Calling backup inventory service to check products is in stock");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String url = "/backup/inventory/check?productIdList=" + String.join(",", productIdList);

        ResponseEntity<List<BackupInventoryResponseDto>> response = inventoryRestTemplate.exchange(url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<BackupInventoryResponseDto>>() {});
        log.info("Inventory service response: {}", response);
        return response.getBody();
    }
}
