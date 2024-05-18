package dev.kei.client;

import dev.kei.dto.AuthTokenValidationResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class AuthServiceClient {
    private final WebClient.Builder webClientBuilder;

    public AuthServiceClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<AuthTokenValidationResponseDto> validateToken(String token) {
        log.info("Calling auth service to validate user token");
        String url = "http://auth-service/api/v1/auth/validate?token=" + token;

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(AuthTokenValidationResponseDto.class);
    }
}
