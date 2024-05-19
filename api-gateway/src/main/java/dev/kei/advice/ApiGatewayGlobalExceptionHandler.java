package dev.kei.advice;

import dev.kei.dto.CustomErrorResponseDto;
import dev.kei.exception.InvalidAuthAccessTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ApiGatewayGlobalExceptionHandler {

    @ExceptionHandler(InvalidAuthAccessTokenException.class)
    public ResponseEntity<CustomErrorResponseDto> handleAuthException(RuntimeException ex) {
        log.info("ApiGatewayGlobalExceptionHandler::handleAuthException exception caught: {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomErrorResponseDto.builder()
                .status("UNAUTHORIZED-ACCESS-TOKEN")
                .code(401)
                .message(ex.getMessage())
                .build());
    }
}
