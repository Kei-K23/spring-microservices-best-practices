package dev.kei.controller;

import dev.kei.dto.UserRequestDto;
import dev.kei.dto.UserResponseDto;
import dev.kei.service.UserService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @RateLimiter(name = "user-service", fallbackMethod = "saveFallback")
    public ResponseEntity<UserResponseDto> save(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRequestDto));
    }

    @GetMapping
    @RateLimiter(name = "user-service", fallbackMethod = "findAllUsersFallback")
    public ResponseEntity<List<UserResponseDto>> findAllProducts() {
        return ResponseEntity.ok().body(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "user-service", fallbackMethod = "findUserByIdFallback")
    public ResponseEntity<UserResponseDto> findProductById(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.findUserById(id));
        }
        catch (Exception ex) {
            if(ex instanceof NoSuchElementException) {
                throw new NoSuchElementException(ex.getMessage());
            } else {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    @PutMapping("/{id}")
    @RateLimiter(name = "user-service", fallbackMethod = "updateFallback")
    public ResponseEntity<UserResponseDto> update(@PathVariable String id,@Valid @RequestBody UserRequestDto userRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, userRequestDto));
        } catch (Exception ex) {
            if(ex instanceof NoSuchElementException) {
                throw new NoSuchElementException(ex.getMessage());
            } else {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "user-service")
    public void delete(@PathVariable String id) {
        try {
            userService.delete(id);
        } catch (Exception ex) {
            if(ex instanceof NoSuchElementException) {
                throw new NoSuchElementException(ex.getMessage());
            } else {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    // Fallback methods
    public ResponseEntity<UserResponseDto> saveFallback(UserRequestDto userRequestDto, Exception ex) {
        handleFallback(ex);
        return null;
    }

    public ResponseEntity<List<UserResponseDto>> findAllUsersFallback(Exception ex) {
        handleFallback(ex);
        return null;
    }

    public ResponseEntity<UserResponseDto> findUserByIdFallback(String id, Exception ex) {
        handleFallback(ex);
        return null;
    }

    public ResponseEntity<UserResponseDto> updateFallback(String id, UserRequestDto userRequestDto, Exception ex) {
        handleFallback(ex);
        return null;
    }

    private void handleFallback(Exception ex) {
        if (ex instanceof NoSuchElementException) {
            log.info("NotFoundException in fallback");
            throw new NoSuchElementException("User not found");
        } else {
            log.info("Rate limit exceeded");
            throw new RuntimeException("You have reached your rate limit. Please try again in 30 seconds.");
        }
    }
}
