package com.muchanga.dev.projecttaskmanagementapi.dto;

import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
        @NotNull String login,
        @NotNull String password

) {
}
