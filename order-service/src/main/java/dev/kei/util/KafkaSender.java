package dev.kei.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaSender {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message, String topicName) {
        log.info("--------------------------------");
        log.info("Sending message:: {},Topic :: {}", message, topicName);
        log.info("--------------------------------");
        kafkaTemplate.send(topicName, message);
    }
}
