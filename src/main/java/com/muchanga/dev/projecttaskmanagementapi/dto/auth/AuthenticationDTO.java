package com.muchanga.dev.projecttaskmanagementapi.dto.auth;

import jakarta.validation.constraints.NotNull;

public record AuthenticationDTO(
        @NotNull String login,
        @NotNull String password
) {
}
