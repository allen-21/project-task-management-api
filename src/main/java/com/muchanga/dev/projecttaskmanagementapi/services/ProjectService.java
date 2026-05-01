package com.muchanga.dev.projecttaskmanagementapi.services;

import com.muchanga.dev.projecttaskmanagementapi.dto.project.*;
import com.muchanga.dev.projecttaskmanagementapi.entity.project.Project;
import com.muchanga.dev.projecttaskmanagementapi.entity.project.ProjectMember;
import com.muchanga.dev.projecttaskmanagementapi.entity.project.ProjectRole;
import com.muchanga.dev.projecttaskmanagementapi.entity.user.User;
import com.muchanga.dev.projecttaskmanagementapi.exception.NoProjectsFoundException;
import com.muchanga.dev.projecttaskmanagementapi.exception.ResourceNotFoundException;
import com.muchanga.dev.projecttaskmanagementapi.repository.ProjectMemberRepository;
import com.muchanga.dev.projecttaskmanagementapi.repository.ProjectRepository;
import com.muchanga.dev.projecttaskmanagementapi.repository.TaskRepository;
import com.muchanga.dev.projecttaskmanagementapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Stream;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository  projectMemberRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository,
                          ProjectMemberRepository projectMemberRepository,
                          TaskRepository taskRepository,
                          UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;

    }

    // criar projeto
    public ProjectResponseDTO create(CreateProjectDTO data, User currentUser){
        Project project = new Project(data.name(), data.description(), currentUser);
        projectRepository.save(project);

        //Cria o ProjectMember com role Owner
        ProjectMember owner = new ProjectMember(project, currentUser, ProjectRole.Owner);
        projectMemberRepository.save(owner);

        return ProjectResponseDTO.form(project);

    }

    //listar apenas os projetos onde o user e menbro
    public List<ProjectResponseDTO> listMyProjects(User currentUser, String name) {
        if (name != null && !name.isBlank()) {
            List<ProjectResponseDTO> result = projectRepository
                    .findAllByMembersUserAndNameContainingIgnoreCase(currentUser, name)
                    .stream()
                    .map(ProjectResponseDTO::form)
                    .toList();
            if (result.isEmpty()) {
                throw new NoProjectsFoundException("Nenhum projeto encontrado com o nome: " + name);
            }
            return result;
        }
        List<ProjectResponseDTO> result = projectRepository.findAllByMembersUser(currentUser)
                .stream()
                .map(ProjectResponseDTO::form)
                .toList();
        if (result.isEmpty()) {
            throw new NoProjectsFoundException("Nemhum projeto vinculado a você");
        }
        return result;
    }


    //Atualizar projecto
    public ProjectResponseDTO update(Long projectId, UpdateProjectDTO data, User currentUser){
        Project project = findProjectAndValidateAccess(projectId,currentUser);
        validateOwner(projectId,currentUser);

        if(data.name() != null && !data.name().isEmpty()){
            project.setName(data.name());
        }
        if(data.description() != null){
            project.setDescription(data.description());
        }

        projectRepository.save(project);
        return ProjectResponseDTO.form(project);
    }

    //deletar projeto
    @Transactional
    public void delete(Long projectId, User currentUser){
        findProjectAndValidateAccess(projectId,currentUser);
        validateOwner(projectId,currentUser);

        taskRepository.deleteAllByProjectId(projectId);
        projectMemberRepository.deleteAllByProjectId(projectId);

        projectRepository.deleteById(projectId);
    }

    //adcionar membros
    public ProjectMemberResponseDTO addMember(Long projectId, AddMemberDTO data,User currentUser){
        findProjectAndValidateAccess(projectId,currentUser);
        validateOwner(projectId,currentUser);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new ResourceNotFoundException("Projeto nao encontrado"));

        User newMember = userRepository.findByLogin(data.login())
                .orElseThrow(()-> new ResourceNotFoundException("Usuario nao encontrado"));

        // checa se ja e membro
        if(projectMemberRepository.existsByProjectIdAndUserId(projectId, newMember.getId())){
            throw new IllegalArgumentException("Usuario ja e membro deste projeto");
        }
        ProjectMember member = new ProjectMember(project, newMember, ProjectRole.MEMBER);
        projectMemberRepository.save(member);

        return ProjectMemberResponseDTO.from(member);
    }

    //remover menbro
    public void removeMember(Long projectId, Long userId, User currentUser){
        findProjectAndValidateAccess(projectId,currentUser);
        validateOwner(projectId,currentUser);

        if(userId.equals(currentUser.getId())){
            throw new IllegalArgumentException("O owner não pode remover a si mesmo do projeto");
        }

        ProjectMember member = projectMemberRepository
                .findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(()-> new ResourceNotFoundException("Membro nao encontrado"));

        projectMemberRepository.delete(member);
    }

    //listar membros
    public List<ProjectMemberResponseDTO> listMembers(Long projectId, String search, User currentUser) {
        findProjectAndValidateAccess(projectId, currentUser);

        if (search != null && !search.isBlank()) {
            List<ProjectMember> byName = projectMemberRepository
                    .findAllByProjectIdAndUserNameContainingIgnoreCase(projectId, search);
            List<ProjectMember> byLogin = projectMemberRepository
                    .findAllByProjectIdAndUserLoginContainingIgnoreCase(projectId, search);

            List<ProjectMemberResponseDTO> result = Stream.concat(byName.stream(), byLogin.stream())
                    .distinct()
                    .map(ProjectMemberResponseDTO::from)
                    .toList();
            if (result.isEmpty()) {
                throw new ResourceNotFoundException("Nenhum membro encontrado com: " + search);
            }
            return result;
        }

        List<ProjectMember> members = projectMemberRepository.findAllByProjectId(projectId)
                .stream()
                .filter(m -> m.getRole() != ProjectRole.Owner)
                .toList();

        if (members.isEmpty()) {
            throw new ResourceNotFoundException("Este projeto ainda não tem nenhum membro alem do owner");
        }
        return members.stream()
                .map(ProjectMemberResponseDTO::from)
                .toList();
    }


    //metodos de validacao

    private Project findProjectAndValidateAccess(Long projectId,User currentUser){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto nao encontrado"));
        if(!projectMemberRepository.existsByProjectIdAndUserId(projectId,currentUser.getId())){
            throw new AccessDeniedException("Voce nao tem acesso a este projeto");
        }
        return project;
    }
    private void validateOwner(Long projectId, User currentUser){
        boolean isOwner = projectMemberRepository.existsByProjectIdAndUserIdAndRole(
                projectId, currentUser.getId(), ProjectRole.Owner);
        if(!isOwner){
            throw new AccessDeniedException("Apenas o owner pode realizar esta acao");
        }

    }
}
