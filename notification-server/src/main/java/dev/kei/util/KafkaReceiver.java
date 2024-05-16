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

    public KafkaReceiver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "notification-service", groupId = "group1")
    public void listener(String data) {
        try {
            log.info("Receive notification :: {} ", data);
        } catch (Exception e) {
            log.error("Failed to deserialize message: {}", data, e);
        }
    }
}
