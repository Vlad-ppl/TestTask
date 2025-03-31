package org.example.testtask.service;

import lombok.RequiredArgsConstructor;
import org.example.testtask.dto.User;
import org.example.testtask.entity.UserEntity;
import org.example.testtask.mapper.UserMapper;
import org.example.testtask.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User getUserByEmail(String email) {
        UserEntity entity = userRepository.findByEmail(email).orElseThrow();
        return UserMapper.toDto(entity);
    }

    public void updateUserProfile(String email, User updatedUser) {
        UserEntity existingUser = userRepository.findByEmail(email).orElseThrow();

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userRepository.save(existingUser);
    }
}
