package com.muchanga.dev.projecttaskmanagementapi.dto.user;

import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
        @NotNull String login,
        @NotNull String password,
        @NotNull String name

) {
}
