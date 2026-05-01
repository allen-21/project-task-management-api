package com.muchanga.dev.projecttaskmanagementapi.repository;

import com.muchanga.dev.projecttaskmanagementapi.entity.project.ProjectMember;
import com.muchanga.dev.projecttaskmanagementapi.entity.project.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    // busca o membro de um projeto específico
    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, Long userId);

    // checa se o usuário é membro do projeto (usado no controle de acesso)
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    // lista todos os membros de um projeto
    List<ProjectMember> findAllByProjectId(Long projectId);

    // checa se o usuário é OWNER do projeto
    boolean existsByProjectIdAndUserIdAndRole(Long projectId, Long userId, ProjectRole role);

    List<ProjectMember> findAllByProjectIdAndUserNameContainingIgnoreCase(Long projectId, String name);
    List<ProjectMember> findAllByProjectIdAndUserLoginContainingIgnoreCase(Long projectId, String login);

    void deleteAllByProjectId(Long projectId);
}
