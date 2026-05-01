package com.muchanga.dev.projecttaskmanagementapi.dto.user;

import com.muchanga.dev.projecttaskmanagementapi.entity.user.User;

public record UserResponseDTO(Long id, String login, String name) {
    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(user.getId(), user.getLogin(), user.getName());
    }
}
