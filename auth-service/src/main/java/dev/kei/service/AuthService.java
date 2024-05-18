package dev.kei.service;

import com.mongodb.DuplicateKeyException;
import dev.kei.dto.AuthRequestDto;
import dev.kei.dto.AuthTokenResponseDto;
import dev.kei.dto.UserRequestDto;
import dev.kei.dto.UserResponseDto;
import dev.kei.entity.User;
import dev.kei.repository.AuthRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AuthService {
    private final AuthRepository authRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthRepository authRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDto save(UserRequestDto userRequestDto) {
        userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        UserResponseDto response = new UserResponseDto();
        try {
            return response.from(authRepository.save(userRequestDto.to(userRequestDto)));
        } catch (DuplicateKeyException ex) {
            throw new IllegalArgumentException("Username already exists");
        }
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAllUsers() {
        return authRepository.findAll().stream().map(this::mapToResponseDto).toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDto findUserById(String id) {
        Optional<User> user = authRepository.findById(id);
        if(user.isEmpty()) {
            throw new NoSuchElementException("User with id " + id + " not found.");
        }
        return mapToResponseDto(user.get());
    }

    @Transactional
    public UserResponseDto update(String id, UserRequestDto userRequestDto) {
        Optional<User> user = authRepository.findById(id);
        if(user.isEmpty()) {
            throw new NoSuchElementException("User with id " + id + " not found to update");
        }
        try {
            User existingUser = user.get();
            existingUser.setName(userRequestDto.getName());
            existingUser.setEmail(userRequestDto.getEmail());
            existingUser.setPassword(userRequestDto.getPassword());

            authRepository.save(existingUser);
            return mapToResponseDto(existingUser);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void delete(String id) {
        Optional<User> user = authRepository.findById(id);
        if(user.isEmpty()) {
            throw new NoSuchElementException("User with id " + id + " not found to delete");
        }
        try {
            authRepository.deleteById(id);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public AuthTokenResponseDto getJWTToken(AuthRequestDto authRequestDto) {
        String jwtToken = jwtService.generateToken(authRequestDto.getName());
        return AuthTokenResponseDto.builder()
                .token(jwtToken)
                .build();
    }

    public void validate(String token) {
        jwtService.validateToken(token);
    }

    private UserResponseDto mapToResponseDto(User user) {
        return user.to(user);
    }
}
