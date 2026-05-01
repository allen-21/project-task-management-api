package com.muchanga.dev.projecttaskmanagementapi.dto.project;

import jakarta.validation.constraints.NotBlank;

public record AddMemberDTO(
        @NotBlank String login
) {
}
