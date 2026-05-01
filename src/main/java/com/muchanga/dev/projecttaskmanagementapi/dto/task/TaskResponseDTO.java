package com.muchanga.dev.projecttaskmanagementapi.dto.task;

import com.muchanga.dev.projecttaskmanagementapi.entity.task.Task;
import com.muchanga.dev.projecttaskmanagementapi.entity.task.TaskPriority;
import com.muchanga.dev.projecttaskmanagementapi.entity.task.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime createdAt,
        LocalDateTime dueDate,
        String createByNome,
        String assignedToNome
) {
    public static  TaskResponseDTO from(Task task) {
        return new TaskResponseDTO(
        task.getId(),
        task.getTitle(),
        task.getDescription(),
        task.getStatus(),
        task.getPriority(),
        task.getCreatedAt(),
        task.getDueDate(),
        task.getCreatedBy().getName(),
        task.getAssignedTo() != null ? task.getAssignedTo().getName() :null

        );
    }
}
