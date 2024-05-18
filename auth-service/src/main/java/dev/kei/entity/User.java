package dev.kei.entity;

import dev.kei.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private String email;
    private String password;

    public UserResponseDto to(User user) {
        return UserResponseDto.builder()
               .id(user.getId())
               .name(user.getName())
               .email(user.getEmail())
               .build();
    }
}
