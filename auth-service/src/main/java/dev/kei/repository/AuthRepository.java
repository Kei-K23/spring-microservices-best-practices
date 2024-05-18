package dev.kei.repository;

import dev.kei.entity.User;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Observed
public interface AuthRepository extends MongoRepository<User, String> {
    Optional<User> findByName(String name);
}
