package dev.kei.filter;

import dev.kei.client.AuthServiceClient;
import dev.kei.exception.InvalidAuthAccessTokenException;
import dev.kei.exception.MissingAuthHeaderException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final AuthServiceClient authServiceClient;

    public AuthenticationFilter(RouteValidator validator, AuthServiceClient authServiceClient) {
        super(Config.class);
        this.validator = validator;
        this.authServiceClient = authServiceClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                // Header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return Mono.error(new MissingAuthHeaderException("Missing authorization header"));
                }

                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                return authServiceClient.validateToken(authHeader)
                        .flatMap(authTokenValidationResponseDto -> {
                            if (!authTokenValidationResponseDto.getCode().equals(HttpStatus.OK.value())) {
                                return Mono.error(new RuntimeException("Invalid token"));
                            }
                            return chain.filter(exchange);
                        })
                        .onErrorResume(e -> {
                            return Mono.error(new InvalidAuthAccessTokenException(e.getMessage()));
                        });
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {

    }
}