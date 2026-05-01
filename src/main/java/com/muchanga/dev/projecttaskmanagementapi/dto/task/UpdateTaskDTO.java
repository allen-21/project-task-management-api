package com.muchanga.dev.projecttaskmanagementapi.dto.task;

import com.muchanga.dev.projecttaskmanagementapi.entity.task.TaskPriority;
import com.muchanga.dev.projecttaskmanagementapi.entity.task.TaskStatus;

import java.time.LocalDateTime;

public record UpdateTaskDTO(
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime dueDate,
        String assignedToLogin
) {
}
