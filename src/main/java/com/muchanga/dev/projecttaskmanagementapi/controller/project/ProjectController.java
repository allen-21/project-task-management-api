package com.muchanga.dev.projecttaskmanagementapi.controller.project;

import com.muchanga.dev.projecttaskmanagementapi.dto.project.*;
import com.muchanga.dev.projecttaskmanagementapi.entity.user.User;
import com.muchanga.dev.projecttaskmanagementapi.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> create(
            @AuthenticationPrincipal User currentUser,
            @RequestBody @Valid CreateProjectDTO data){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.create(data, currentUser));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> listMyProjects(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String name){
        return ResponseEntity.ok(projectService.listMyProjects(currentUser, name));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> update(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User currentUser,
            @RequestBody @Valid UpdateProjectDTO data){
        return ResponseEntity.ok(projectService.update(projectId, data, currentUser));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User currentUser){
        projectService.delete(projectId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity<ProjectMemberResponseDTO> addMember(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User currentUser,
            @RequestBody @Valid AddMemberDTO data){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.addMember(projectId, data, currentUser));
    }

    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<ProjectMemberResponseDTO>> listMembers(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String search){
        return ResponseEntity.ok(projectService.listMembers(projectId, search, currentUser));
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            @AuthenticationPrincipal User currentUser) {
        projectService.removeMember(projectId, userId, currentUser);
        return ResponseEntity.noContent().build();
    }


}
