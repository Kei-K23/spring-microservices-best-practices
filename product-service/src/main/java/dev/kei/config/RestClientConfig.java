package dev.kei.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

    // rest client configuration for inventory service
    @Bean
    @LoadBalanced
    public RestTemplate inventoryRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.rootUri("http://inventory-service/api/v1").build();
    }
}
