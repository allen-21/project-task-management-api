package com.muchanga.dev.projecttaskmanagementapi.controller.project;

import com.muchanga.dev.projecttaskmanagementapi.dto.task.CreateTaskDTO;
import com.muchanga.dev.projecttaskmanagementapi.dto.task.TaskResponseDTO;
import com.muchanga.dev.projecttaskmanagementapi.dto.task.UpdateTaskDTO;
import com.muchanga.dev.projecttaskmanagementapi.entity.task.TaskStatus;
import com.muchanga.dev.projecttaskmanagementapi.entity.user.User;
import com.muchanga.dev.projecttaskmanagementapi.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController {

    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(
            @PathVariable long projectId,
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateTaskDTO data){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.create(projectId, data, user));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> list(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Long assignedTo) {

        if (status != null) {
            return ResponseEntity.ok(taskService.listByStatus(projectId, status, currentUser));
        }
        if (assignedTo != null) {
            return ResponseEntity.ok(taskService.listByAssignedUser(projectId, assignedTo, currentUser));
        }
        if (title != null && !title.isBlank()) {
            return ResponseEntity.ok(taskService.listByProject(projectId, title, currentUser));
        }
        return ResponseEntity.ok(taskService.listByProject(projectId, null, currentUser));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> getById(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @AuthenticationPrincipal User currentUser){
        return ResponseEntity.ok(taskService.getById(projectId, taskId, currentUser));
    }
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> update(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @AuthenticationPrincipal User currentUser,
            @RequestBody @Valid UpdateTaskDTO data){
        return ResponseEntity.ok(taskService.update(projectId, taskId, data, currentUser));
    }
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @AuthenticationPrincipal User currentUser){
        taskService.delete(projectId, taskId, currentUser);
        return ResponseEntity.noContent().build();
    }

}
