package dev.kei.repository;

import dev.kei.entity.Order;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Observed
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomerId(String customerId);
}
