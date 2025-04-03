package org.example.testtask.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
}
