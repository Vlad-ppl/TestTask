package org.example.testtask.service;

import lombok.AllArgsConstructor;
import org.example.testtask.dto.User;
import org.example.testtask.entity.UserEntity;
import org.example.testtask.mapper.UserMapper;
import org.example.testtask.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) return false;

        UserEntity userEntity = UserMapper.toEntity(user);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword())); // Шифруем
        userRepository.save(userEntity);
        return true;
    }
}
