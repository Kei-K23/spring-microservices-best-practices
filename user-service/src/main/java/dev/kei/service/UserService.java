package dev.kei.service;

import dev.kei.dto.UserRequestDto;
import dev.kei.dto.UserResponseDto;
import dev.kei.entity.User;
import dev.kei.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDto save(UserRequestDto userRequestDto) {
        String password = userRequestDto.getPassword();
        // move slat to .env
        String hashPassword =
                BCrypt.hashpw(password, "f0b581ed73a0b46c54c19d4a553f9f661e3738fab7b28577cb101c2fe20262f9bcec5de7dd655ac5bba534639f95872d6d32cee0947a4d17ed7515bfaa5ddcb1");
        userRequestDto.setPassword(hashPassword);
        UserResponseDto response = new UserResponseDto();
        return response.from(userRepository.save(userRequestDto.to(userRequestDto)));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAll().stream().map(this::mapToResponseDto).toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDto findUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new NoSuchElementException("User with id " + id + " not found.");
        }
        return mapToResponseDto(user.get());
    }

    @Transactional
    public UserResponseDto update(String id, UserRequestDto userRequestDto) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new NoSuchElementException("User with id " + id + " not found to update");
        }
        try {
            User existingUser = user.get();
            existingUser.setName(userRequestDto.getName());
            existingUser.setEmail(userRequestDto.getEmail());
            existingUser.setPassword(userRequestDto.getPassword());

            userRepository.save(existingUser);
            return mapToResponseDto(existingUser);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void delete(String id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new NoSuchElementException("User with id " + id + " not found to delete");
        }
        try {
            userRepository.deleteById(id);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private UserResponseDto mapToResponseDto(User user) {
        return user.to(user);
    }
}
