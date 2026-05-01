package com.muchanga.dev.projecttaskmanagementapi.services;

import com.muchanga.dev.projecttaskmanagementapi.dto.user.UserResponseDTO;
import com.muchanga.dev.projecttaskmanagementapi.dto.user.UserUpdateDTO;
import com.muchanga.dev.projecttaskmanagementapi.entity.user.User;
import com.muchanga.dev.projecttaskmanagementapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    //busca perfil proprio
    public UserResponseDTO getProflile(User currentUser) {
        return UserResponseDTO.from(currentUser);
    }

    //atualiza
    public UserResponseDTO upadate(User currentUser, UserUpdateDTO data) {
        if(data.name() != null && !data.name().isBlank()){
            currentUser.setName(data.name());
        }
        if(data.password() != null && !data.password().isBlank()){
            currentUser.setPassword(passwordEncoder.encode(data.password()));
        }
        userRepository.save(currentUser);
        return UserResponseDTO.from(currentUser);
    }
    public void delete(User currentUser) {
        userRepository.delete(currentUser);
    }
}
