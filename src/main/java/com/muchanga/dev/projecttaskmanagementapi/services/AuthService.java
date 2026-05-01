package com.muchanga.dev.projecttaskmanagementapi.services;

import com.muchanga.dev.projecttaskmanagementapi.dto.user.RegisterDTO;
import com.muchanga.dev.projecttaskmanagementapi.entity.user.User;
import com.muchanga.dev.projecttaskmanagementapi.exception.LoginAlreadyExistsException;
import com.muchanga.dev.projecttaskmanagementapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterDTO data){
        if(userRepository.findByLogin(data.login()).isPresent()){
            throw new LoginAlreadyExistsException(data.login());
        }
        String encodedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(data.login(), encodedPassword, data.name());
        userRepository.save(newUser);

    }
}
