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
public class AuthServiceGlobalExceptionHandler {

    // handle too many requests exception
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomErrorResponseDto> handleTooManyRequestException(RuntimeException ex) {
        log.info("AuthServiceGlobalExceptionHandler::handleTooManyRequestException exception caught: {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(CustomErrorResponseDto.builder()
                .status("TOO-MANY-REQUESTS")
                .code(429)
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CustomErrorResponseDto> handleNotFoundException(RuntimeException ex) {
        log.info("AuthServiceGlobalExceptionHandler::handleNotFoundException exception caught: {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomErrorResponseDto.builder()
                .status("USER-NOT-FOUND")
                .code(404)
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(InvalidAuthAccessTokenException.class)
    public ResponseEntity<CustomErrorResponseDto> handleAuthException(RuntimeException ex) {
        log.info("AuthServiceGlobalExceptionHandler::handleAuthException exception caught: {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomErrorResponseDto.builder()
                .status("UNAUTHORIZED-ACCESS-TOKEN")
                .code(401)
                .message(ex.getMessage())
                .build());
    }
}
