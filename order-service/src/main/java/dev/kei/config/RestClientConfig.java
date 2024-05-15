package dev.kei.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

    // rest client configuration for inventory service
    @Bean
    public RestTemplate inventoryRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        // TODO change uri when after adding API Gateway
        return restTemplateBuilder.rootUri("http://localhost:8082/api/v1").build();
    }
}
