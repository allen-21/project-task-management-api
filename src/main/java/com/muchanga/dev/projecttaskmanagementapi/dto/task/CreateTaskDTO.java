package com.muchanga.dev.projecttaskmanagementapi.dto.task;

import com.muchanga.dev.projecttaskmanagementapi.entity.task.TaskPriority;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CreateTaskDTO(
        @NotBlank String title,
        String description,
        TaskPriority priority,
        LocalDateTime dueDate,
        String assignedToLogin
) {
}
