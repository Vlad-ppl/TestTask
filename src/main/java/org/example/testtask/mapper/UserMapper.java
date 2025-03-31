package org.example.testtask.mapper;

import org.example.testtask.dto.User;
import org.example.testtask.entity.UserEntity;

public class UserMapper {

    public static User toDto(UserEntity entity) {
        if (entity == null) return null;

        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .userRole(entity.getUserRole())
                .build();
    }

    public static UserEntity toEntity(User dto) {
        if (dto == null) return null;

        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setUserRole(dto.getUserRole());
        return entity;
    }
}
