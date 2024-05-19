package dev.kei.repository;

import dev.kei.entity.Product;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Observed
public interface ProductRepository extends MongoRepository<Product, String> {
}
