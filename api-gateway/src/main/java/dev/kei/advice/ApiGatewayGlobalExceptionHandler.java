package dev.kei.advice;

import dev.kei.dto.CustomErrorResponseDto;
import dev.kei.exception.InvalidAuthAccessTokenException;
import dev.kei.exception.MissingAuthHeaderException;
import dev.kei.exception.ServiceUnavailableException;
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
    public ResponseEntity<CustomErrorResponseDto> handleAuthException(InvalidAuthAccessTokenException ex) {
        log.info("ApiGatewayGlobalExceptionHandler::handleAuthException exception caught: {} ", ex.getMessage());
        return ResponseEntity.status(403).body(CustomErrorResponseDto.builder()
                .status("UNAUTHORIZED-ACCESS-TOKEN")
                .code(403)
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(MissingAuthHeaderException.class)
    public ResponseEntity<CustomErrorResponseDto> handleMissingAuthHeaderException(MissingAuthHeaderException ex) {
        log.info("ApiGatewayGlobalExceptionHandler::handleMissingAuthHeaderException exception caught: {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomErrorResponseDto.builder()
                .status("MISSING-AUTH-TOKEN")
                .code(401)
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<CustomErrorResponseDto> handleServiceUnavailableException(ServiceUnavailableException ex) {
        log.info("ApiGatewayGlobalExceptionHandler::handleServiceUnavailableException exception caught: {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(CustomErrorResponseDto.builder()
                .status("SERVICE-UNAVAILABLE")
                .code(503)
                .message(ex.getMessage())
                .build());
    }
}
