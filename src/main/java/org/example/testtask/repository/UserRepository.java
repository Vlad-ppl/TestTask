package org.example.testtask.repository;

import org.example.testtask.entity.UserEntity;
import org.example.testtask.entity.role.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findAll();
    List<UserEntity> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);
    List<UserEntity> findByUserRole(UserRole role);

}
