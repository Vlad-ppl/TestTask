package org.example.testtask.service;

import lombok.RequiredArgsConstructor;
import org.example.testtask.dto.User;
import org.example.testtask.entity.UserEntity;
import org.example.testtask.entity.role.UserRole;
import org.example.testtask.mapper.UserMapper;
import org.example.testtask.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByUserRole(role).stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public User getUserById(Long id) {
        UserEntity entity = userRepository.findById(id).orElseThrow();
        return UserMapper.toDto(entity);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUser(User user) {
        UserEntity existingUser = userRepository.findById(user.getId()).orElseThrow();

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setUserRole(user.getUserRole());

        userRepository.save(existingUser);
    }
}
