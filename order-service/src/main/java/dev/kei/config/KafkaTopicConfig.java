package dev.kei.config;

import lombok.experimental.WithBy;
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

    @Bean
    public NewTopic backupCreateOrderServiceTopic() {
        return TopicBuilder.name("backup-create-order-service").build();
    }

    public NewTopic backupUpdateOrderServiceTopic() {
        return TopicBuilder.name("backup-update-order-service").build();
    }

    public NewTopic backupDeleteOrderServiceTopic() {
        return TopicBuilder.name("backup-delete-order-service").build();
    }

}
