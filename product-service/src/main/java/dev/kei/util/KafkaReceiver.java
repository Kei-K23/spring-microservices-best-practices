package dev.kei.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kei.dto.OrderItemDto;
import dev.kei.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class KafkaReceiver {

    private final ObjectMapper objectMapper;
    private final ProductService productService;

    public KafkaReceiver(ObjectMapper objectMapper, ProductService productService) {
        this.objectMapper = objectMapper;
        this.productService = productService;
    }

    @KafkaListener(topics = "product-service", groupId = "group1")
    public void listener(String data) {
        try {
            List<OrderItemDto> orderItemDtoList = objectMapper.readValue(data, new TypeReference<List<OrderItemDto>>() {});
            log.info("Received message {} in product-service group1", orderItemDtoList);
            orderItemDtoList.forEach(orderItemDto -> productService.updateStock(orderItemDto.getProductId(), orderItemDto.getQuantity()));
            log.info("Successfully updated the product stock in product-service group1");
        } catch (Exception e) {
            log.error("Failed to deserialize message: {}", data, e);
        }
    }
}
