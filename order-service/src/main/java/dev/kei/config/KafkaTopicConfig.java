package dev.kei.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic productServiceTopic() {
        return TopicBuilder.name("product-service").build();
    }

    @Bean
    public NewTopic inventoryServiceTopic() {
        return TopicBuilder.name("inventory-service").build();
    }

    @Bean
    public NewTopic notificationServiceTopic() {
        return TopicBuilder.name("notification-service").build();
    }
}
