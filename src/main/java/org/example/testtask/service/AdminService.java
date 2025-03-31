package org.example.testtask.service;

import lombok.RequiredArgsConstructor;
import org.example.testtask.entity.UserEntity;
import org.example.testtask.entity.role.UserRole;
import org.example.testtask.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public List<UserEntity> getUsersByRole(UserRole role) {
        return userRepository.findByUserRole(role);
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUser(UserEntity user) {
        UserEntity existingUser = userRepository.findById(user.getId()).orElseThrow();
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setUserRole(user.getUserRole());
        userRepository.save(existingUser);
    }
}
