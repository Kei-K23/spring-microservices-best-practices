package dev.kei.controller;

import dev.kei.dto.*;
import dev.kei.exception.ExceedRateLimitException;
import dev.kei.exception.InvalidAuthAccessTokenException;
import dev.kei.service.AuthService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    @RateLimiter(name = "auth-service", fallbackMethod = "saveFallback")
    public ResponseEntity<UserResponseDto> save(@RequestBody UserRequestDto userRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(authService.save(userRequestDto));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @PostMapping("/token")
    @RateLimiter(name = "auth-service", fallbackMethod = "getJWTTokenFallback")
    public ResponseEntity<AuthTokenResponseDto> getJWTToken(@RequestBody AuthRequestDto authRequestDto) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getName(), authRequestDto.getPassword()));
        if(authenticate.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.OK).body(authService.getJWTToken(authRequestDto));
        } else {
            throw new InvalidAuthAccessTokenException("Invalid access");
        }
    }

    @GetMapping(value = "/validate", params = "token")
    @RateLimiter(name = "auth-service", fallbackMethod = "validateFallback")
    public ResponseEntity<AuthTokenValidationResponseDto> validate(@RequestParam(name = "token") String token) {
        try {
            authService.validate(token);
            return ResponseEntity.status(HttpStatus.OK).body(AuthTokenValidationResponseDto.builder()
                    .code(HttpStatus.OK.value())
                    .message("Valid token")
                    .build());
        } catch (Exception ex) {
            throw new InvalidAuthAccessTokenException("Invalid access token");
        }
    }

    @GetMapping
    @RateLimiter(name = "auth-service", fallbackMethod = "findAllUsersFallback")
    public ResponseEntity<List<UserResponseDto>> findAllProducts() {
        return ResponseEntity.ok().body(authService.findAllUsers());
    }

    @GetMapping("/{id}")
    @RateLimiter(name = "auth-service", fallbackMethod = "findUserByIdFallback")
    public ResponseEntity<UserResponseDto> findProductById(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(authService.findUserById(id));
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
    @RateLimiter(name = "auth-service", fallbackMethod = "updateFallback")
    public ResponseEntity<UserResponseDto> update(@PathVariable String id,@Valid @RequestBody UserRequestDto userRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(authService.update(id, userRequestDto));
        } catch (Exception ex) {
            if(ex instanceof NoSuchElementException) {
                throw new NoSuchElementException(ex.getMessage());
            } else {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    @DeleteMapping("/{id}")
    @RateLimiter(name = "auth-service")
    public void delete(@PathVariable String id) {
        try {
            authService.delete(id);
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

    public ResponseEntity<AuthTokenResponseDto> getJWTTokenFallback(AuthRequestDto authRequestDto, Exception ex) {
        handleFallback(ex);
        return null;
    }

    public ResponseEntity<AuthTokenValidationResponseDto> validateFallback(String token, Exception ex) {
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
        } else if(ex instanceof RequestNotPermitted) {
            log.info("RequestNotPermitted in fallback");
            throw new ExceedRateLimitException("You have reached your rate limit. Please try again in 30 seconds.");
        } else if (ex instanceof InvalidAuthAccessTokenException) {
            log.info("InvalidAuthAccessTokenException in fallback");
            throw new InvalidAuthAccessTokenException("Invalid access token");
        } else {
            log.info("Rate limit exceeded");
            throw new RuntimeException("You have reached your rate limit. Please try again in 30 seconds.");
        }
    }
}
