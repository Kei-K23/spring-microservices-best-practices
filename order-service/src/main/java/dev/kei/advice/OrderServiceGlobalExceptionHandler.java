package dev.kei.advice;

import dev.kei.dto.CustomErrorResponseDto;
import dev.kei.exception.OtherServiceCallException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class OrderServiceGlobalExceptionHandler {

    // handle too many requests exception
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomErrorResponseDto> handleTooManyRequestException(RuntimeException ex) {
        log.info("OrderServiceGlobalExceptionHandler::handleTooManyRequestException exception caught: {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(CustomErrorResponseDto.builder()
                .status("TOO-MANY-REQUESTS")
                .code(429)
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CustomErrorResponseDto> handleNotFoundException(RuntimeException ex) {
        log.info("OrderServiceGlobalExceptionHandler::handleNotFoundException exception caught: {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomErrorResponseDto.builder()
                .status("ORDER-NOT-FOUND")
                .code(404)
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(OtherServiceCallException.class)
    public ResponseEntity<CustomErrorResponseDto> handleOtherServiceCallException(RuntimeException ex) {
        log.info("OrderServiceGlobalExceptionHandler::handleOtherServiceCallException exception caught: {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CustomErrorResponseDto.builder()
                .status("ORDER-CANNOT-PLACE")
                .code(500)
                .message(ex.getMessage())
                .build());
    }
}
