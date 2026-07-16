package org.ats.services;

import lombok.RequiredArgsConstructor;
import org.ats.dto.UserRegisterRequest;
import org.ats.dto.UserRequest;
import org.ats.entities.User;
import org.ats.exceptions.ObjectInvalidException;
import org.ats.mapper.UserMapper;
import org.ats.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Qualifier("authService")
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository; // DI
    private final UserMapper userMapper;

    @Override
    public User authenticate(UserRequest userRequest) {
        User user = userRepository.findByEmailAndPass(userRequest.getEmail(),
                userRequest.getPassword()).orElseThrow(() -> {
            return new RuntimeException("Email or password is invalid!");
        });

        return user;
    }

    @Override
    public Long register(UserRegisterRequest registerRequest) {
        // Validate
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ObjectInvalidException("Email is already existed.");
        }


        // Convert to Entity
        return userRepository.save(userMapper.toEntity(registerRequest)).getId();
    }
}
