package com.muchanga.dev.projecttaskmanagementapi.dto.project;

import com.muchanga.dev.projecttaskmanagementapi.entity.project.Project;
import com.muchanga.dev.projecttaskmanagementapi.entity.project.ProjectMember;
import com.muchanga.dev.projecttaskmanagementapi.entity.project.ProjectRole;

public record ProjectMemberResponseDTO(
        Long userid,
        String name,
        String login,
        ProjectRole role
) {
    public static ProjectMemberResponseDTO from(ProjectMember member) {
        return  new ProjectMemberResponseDTO(
                member.getUser().getId(),
                member.getUser().getName(),
                member.getUser().getLogin(),
                member.getRole()
        );
    }
}
