package org.example.testtask.service;

import lombok.RequiredArgsConstructor;
import org.example.testtask.entity.UserEntity;
import org.example.testtask.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public void updateUserProfile(UserEntity existingUser, UserEntity updatedUser) {
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userRepository.save(existingUser);
    }
}
