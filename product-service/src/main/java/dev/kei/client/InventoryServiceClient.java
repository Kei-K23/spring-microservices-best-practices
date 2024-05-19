package dev.kei.client;

import com.netflix.discovery.EurekaClient;
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
public class InventoryServiceClient {
    private final RestTemplate inventoryRestTemplate;

    public InventoryServiceClient(RestTemplate inventoryRestTemplate) {
        this.inventoryRestTemplate = inventoryRestTemplate;
    }

    public Long createInventoryItemFromProduct(InventoryRequestDto inventoryRequestDto) {
        log.info("Calling inventory service to create inventory item");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<InventoryRequestDto> requestEntity = new HttpEntity<>(inventoryRequestDto, headers);
        var response = inventoryRestTemplate.exchange("/inventory",
                HttpMethod.POST,
                requestEntity,
                InventoryResponseDto.class).getBody();
        log.info("Inventory service response: {}", response);
        return response.getId();
    }

    public void updateInventoryItemFromProduct(InventoryRequestDto inventoryRequestDto) {
        log.info("Calling inventory service to update inventory item");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<InventoryRequestDto> requestEntity = new HttpEntity<>(inventoryRequestDto, headers);
        var response = inventoryRestTemplate.exchange("/inventory?productId=" + inventoryRequestDto.getProductId(),
                HttpMethod.PUT,
                requestEntity,
                InventoryResponseDto.class).getBody();
        log.info("Inventory service response: {}", response);
    }

    public void deleteInventoryItemFromProduct(String productId) {
        log.info("Calling inventory service to delete inventory item");

        // Create headers if needed
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        var response = inventoryRestTemplate.exchange(
                "/inventory?productId=" + productId,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        log.info("Inventory service response: {}", response);
    }
}
