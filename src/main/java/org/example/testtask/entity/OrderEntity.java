package org.example.testtask.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.testtask.entity.role.OrderRole;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_role")
    private OrderRole orderRole = OrderRole.NEW;

    @ManyToMany
    @JoinTable(
            name = "order_items",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<ItemEntity> items;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
