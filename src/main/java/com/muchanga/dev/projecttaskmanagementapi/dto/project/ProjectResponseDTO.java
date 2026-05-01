package com.muchanga.dev.projecttaskmanagementapi.dto.project;

import com.muchanga.dev.projecttaskmanagementapi.entity.project.Project;

import java.time.LocalDateTime;

public record ProjectResponseDTO(
        Long id,
        String name,
        String description,
        String creatorName,
        LocalDateTime created
) {
    public static ProjectResponseDTO form(Project project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreator().getName(),
                project.getCreatedAt()

        );
    }
}
