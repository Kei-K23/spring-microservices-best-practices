package dev.kei.controller;

import dev.kei.dto.OrderRequestDto;
import dev.kei.dto.OrderResponseDto;
import dev.kei.dto.OrderStatusUpdateRequestDto;
import dev.kei.exception.OtherServiceCallException;
import dev.kei.service.OrderService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @RateLimiter(name = "order-service", fallbackMethod = "saveFallback")
    public ResponseEntity<OrderResponseDto> save(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(orderRequestDto));
        } catch (Exception ex) {
            if(ex instanceof NoSuchElementException) {
                throw new NoSuchElementException(ex.getMessage());
            } else if (ex instanceof OtherServiceCallException) {
                throw new OtherServiceCallException(ex.getMessage());
            } else {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    @GetMapping
    @RateLimiter(name = "order-service", fallbackMethod = "findAllOrdersFallback")
    public ResponseEntity<List<OrderResponseDto>> findAllOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findAllOrders());
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "order-service", fallbackMethod = "findOrderByIdFallback")
    public ResponseEntity<OrderResponseDto> findOrderById(@PathVariable(name = "id") Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(orderService.findOrderById(id));
        }
        catch (Exception ex) {
            if(ex instanceof NoSuchElementException) {
                throw new NoSuchElementException(ex.getMessage());
            } else {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    @GetMapping(params = "customerId")
    @RateLimiter(name = "order-service", fallbackMethod = "findOrderByIdFallback")
    public ResponseEntity<List<OrderResponseDto>> findOrdersByCustomerId(@RequestParam(name = "customerId") String customerId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(orderService.findOrdersByCustomerId(customerId));
        } catch (Exception ex) {
            if(ex instanceof NoSuchElementException) {
                throw new NoSuchElementException(ex.getMessage());
            } else {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    @PutMapping("/{id}")
    @RateLimiter(name = "order-service", fallbackMethod = "updateFallback")
    public ResponseEntity<OrderResponseDto> update(@PathVariable(name = "id") Long id,@Valid @RequestBody OrderRequestDto orderRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(orderService.update(id, orderRequestDto));
        } catch (Exception ex) {
            if(ex instanceof NoSuchElementException) {
                throw new NoSuchElementException(ex.getMessage());
            } else {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    @PutMapping("/status/{id}")
    @RateLimiter(name = "order-service", fallbackMethod = "orderStatusUpdateFallback")
    public ResponseEntity<OrderResponseDto> orderStatusUpdate(@PathVariable(name = "id") Long id,@Valid @RequestBody OrderStatusUpdateRequestDto orderStatusUpdateRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(orderService.orderStatusUpdate(id, orderStatusUpdateRequestDto));
        } catch (Exception ex) {
            if(ex instanceof NoSuchElementException) {
                throw new NoSuchElementException(ex.getMessage());
            } else {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "order-service")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") Long id) {
        orderService.delete(id);
    }

    // Fallback methods
    public ResponseEntity<OrderResponseDto> saveFallback(OrderRequestDto orderRequestDto, Exception ex) {
        handleFallback(ex);
        return null;
    }

    public ResponseEntity<List<OrderResponseDto>> findAllOrdersFallback(Exception ex) {
        handleFallback(ex);
        return null;
    }

    public ResponseEntity<OrderResponseDto> findOrderByIdFallback(String id, Exception ex) {
        handleFallback(ex);
        return null;
    }

    public ResponseEntity<OrderResponseDto> updateFallback(String id, OrderRequestDto orderRequestDto, Exception ex) {
        handleFallback(ex);
        return null;
    }
    public ResponseEntity<OrderResponseDto> orderStatusUpdateFallback(String id, OrderStatusUpdateRequestDto orderStatusUpdateRequestDto, Exception ex) {
        handleFallback(ex);
        return null;
    }

    private void handleFallback(Exception ex) {
        if (ex instanceof NoSuchElementException) {
            log.info("NotFoundException in fallback");
            throw new NoSuchElementException("Order not found");
        } else if(ex instanceof OtherServiceCallException) {
            log.info("OtherServiceCallException in fallback");
            throw new OtherServiceCallException(ex.getMessage());
        } else {
            log.info("Rate limit exceeded");
            throw new RuntimeException("You have reached your rate limit. Please try again in 30 seconds.");
        }
    }
}
