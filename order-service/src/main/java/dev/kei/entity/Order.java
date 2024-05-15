package dev.kei.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String customerId;
    @Column(name = "order_code", unique = true)
    private String orderCode;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", columnDefinition = "varchar(255) default 'PENDING'")
    @NotNull
    private OrderStatus orderStatus;
    @OneToMany(cascade = CascadeType.ALL)
    @NotNull
    private List<OrderItem> orderItems;
}
