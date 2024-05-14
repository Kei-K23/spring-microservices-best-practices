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
public class InventoryServiceClient {
    private final RestTemplate inventoryRestTemplate;

    public InventoryServiceClient(RestTemplate inventoryRestTemplate) {
        this.inventoryRestTemplate = inventoryRestTemplate;
    }

    public InventoryResponseDto createInventoryItemFromProduct(InventoryRequestDto inventoryRequestDto) {
        log.info("Calling inventory service");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<InventoryRequestDto> requestEntity = new HttpEntity<>(inventoryRequestDto, headers);


        var response = inventoryRestTemplate.exchange("/inventory",
                HttpMethod.POST,
                requestEntity,
                InventoryResponseDto.class).getBody();
        log.info("Inventory service response: {}", response);
        return response;
    }
}
