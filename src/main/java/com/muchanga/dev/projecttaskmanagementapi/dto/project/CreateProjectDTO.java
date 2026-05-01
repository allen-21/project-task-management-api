package com.muchanga.dev.projecttaskmanagementapi.dto.project;

import jakarta.validation.constraints.NotBlank;

public record CreateProjectDTO(
        @NotBlank String name,
        String description
) {
}
