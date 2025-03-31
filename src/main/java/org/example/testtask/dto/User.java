package org.example.testtask.dto;

import lombok.*;
import org.example.testtask.entity.role.UserRole;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole userRole = UserRole.USER;
}
