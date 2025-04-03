package org.example.testtask.dto;

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
@Data
public class Order {
    private Long id;
    private String orderName;
    private OrderRole orderRole;
    private List<Item> items;
}
