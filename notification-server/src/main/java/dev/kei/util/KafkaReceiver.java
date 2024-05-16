package dev.kei.util;

import dev.kei.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaReceiver {

    private final EmailService emailService;

    public KafkaReceiver(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "notification-service", groupId = "group1")
    public void listener(String data) {
        try {
            log.info("Receive notification :: {} ", data);
            emailService.sendHtmlEmail("test@example.com", "Order status updated", data);
            log.info("Successfully send the email");
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }
}
