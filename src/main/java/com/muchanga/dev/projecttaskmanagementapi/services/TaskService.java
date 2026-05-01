package com.muchanga.dev.projecttaskmanagementapi.services;

import com.muchanga.dev.projecttaskmanagementapi.dto.task.CreateTaskDTO;
import com.muchanga.dev.projecttaskmanagementapi.dto.task.TaskResponseDTO;
import com.muchanga.dev.projecttaskmanagementapi.dto.task.UpdateTaskDTO;
import com.muchanga.dev.projecttaskmanagementapi.entity.project.Project;
import com.muchanga.dev.projecttaskmanagementapi.entity.project.ProjectRole;
import com.muchanga.dev.projecttaskmanagementapi.entity.task.Task;
import com.muchanga.dev.projecttaskmanagementapi.entity.task.TaskStatus;
import com.muchanga.dev.projecttaskmanagementapi.entity.user.User;
import com.muchanga.dev.projecttaskmanagementapi.exception.NoTasksFoundException;
import com.muchanga.dev.projecttaskmanagementapi.exception.ResourceNotFoundException;
import com.muchanga.dev.projecttaskmanagementapi.repository.ProjectMemberRepository;
import com.muchanga.dev.projecttaskmanagementapi.repository.ProjectRepository;
import com.muchanga.dev.projecttaskmanagementapi.repository.TaskRepository;
import com.muchanga.dev.projecttaskmanagementapi.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;


    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository, ProjectMemberRepository projectMemberRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    // criar tarefa
    public TaskResponseDTO create(Long projectId, CreateTaskDTO data, User currentUser) {
        Project project = findProjectAndValidateAccess(projectId,currentUser);
        User assignedTo = resolveAssignedTo(projectId, data.assignedToLogin());

        Task task = new Task(data.title(),data.description(),data.priority(),
                data.dueDate(),project,currentUser);
        task.setAssignedTo(assignedTo);
        taskRepository.save(task);

        return TaskResponseDTO.from(task);

    }

    //listar todas as tarefas do projeto(com busca opcional por titulo)
    public List<TaskResponseDTO> listByProject(Long projectId,String title, User currentUser) {
        findProjectAndValidateAccess(projectId,currentUser);

        if(title != null && !title.isBlank()){
            List<TaskResponseDTO> result = taskRepository
                    .findAllByProjectIdAndTitleContainingIgnoreCase(projectId,title)
                    .stream()
                    .map(TaskResponseDTO::from)
                    .toList();
            if (result.isEmpty()){
                throw new NoTasksFoundException("Nenhuma tarefa encontrada com o título:"  + title);
            }
            return result;
        }
        List<TaskResponseDTO> result = taskRepository.findAllByProjectId(projectId)
                .stream()
                .map(TaskResponseDTO::from)
                .toList();
        if(result.isEmpty()){
            throw new NoTasksFoundException("Este projeto ainda não tem nenhuma tarefa");
        }
        return result;
    }

    //lista as tarefas por status

    public List<TaskResponseDTO> listByStatus(Long projectId, TaskStatus status, User currentUser) {
        findProjectAndValidateAccess(projectId,currentUser);

        List<TaskResponseDTO> result = taskRepository.findAllByProjectIdAndStatus(projectId,status)
                .stream()
                .map(TaskResponseDTO::from)
                .toList();
        if (result.isEmpty()){
            throw new NoTasksFoundException("Nenhuma tarefa encontrada com o status: " + status);
        }
        return result;
    }

    //list tarefas do usuario no projecto
    public List<TaskResponseDTO> listByAssignedUser(Long projectId, Long userId, User currentUser) {
        findProjectAndValidateAccess(projectId,currentUser);

        List<TaskResponseDTO> result = taskRepository
                .findAllByProjectIdAndAssignedToId(projectId, userId)
                .stream()
                .map(TaskResponseDTO::from)
                .toList();
        if (result.isEmpty()){
            throw new NoTasksFoundException("Nenhuma tarefa atribuída a este utilizador");
        }
        return result;
    }

    //buscar tarefa por id
    public TaskResponseDTO getById(Long projectId, Long taskId, User currentUser) {
        findProjectAndValidateAccess(projectId,currentUser);
        Task task = findTaskInProject(taskId, projectId);
        return TaskResponseDTO.from(task);
    }

    //atualizar tarefa
    public TaskResponseDTO update(Long projectId, Long taskId, UpdateTaskDTO data, User currentUser) {
        findProjectAndValidateAccess(projectId,currentUser);
        Task task = findTaskInProject(taskId, projectId);

        validateTaskEditPermission(task,projectId,currentUser);

        if (data.title() != null && !data.title().isBlank()) {
            task.setTitle(data.title());
        }
        if (data.description() != null) {
            task.setDescription(data.description());
        }
        if (data.status() != null) {
            task.setStatus(data.status());
        }
        if (data.priority() != null) {
            task.setPriority(data.priority());
        }
        if (data.dueDate() != null) {
            task.setDueDate(data.dueDate());
        }
        if (data.assignedToLogin() != null && !data.assignedToLogin().isBlank()) {
            User assignedTo = userRepository.findByLogin(data.assignedToLogin())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

            if (!projectMemberRepository.existsByProjectIdAndUserId(projectId, assignedTo.getId())) {
                throw new IllegalArgumentException("O usuário atribuído não é membro deste projeto");
            }
            task.setAssignedTo(assignedTo);
        }

        taskRepository.save(task);
        return TaskResponseDTO.from(task);
    }

    //deletar tarefa
    public void delete(Long projectId, Long taskId, User currentUser) {
        findProjectAndValidateAccess(projectId,currentUser);
        Task task = findTaskInProject(taskId, projectId);
        validateTaskEditPermission(task,projectId,currentUser);
        taskRepository.delete(task);
    }

    //metodo de validacao
    private Project findProjectAndValidateAccess(Long projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new ResourceNotFoundException("Projeto não encontrado"));
        if (!projectMemberRepository.existsByProjectIdAndUserId(projectId, currentUser.getId())) {
            throw new AccessDeniedException("Você não tem acesso a este projeto");
        }
        return project;
    }
    private Task findTaskInProject(Long taskId, Long projectId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()-> new ResourceNotFoundException("Tarefa não encontrado"));

        if (!task.getProject().getId().equals(projectId)) {
            throw new AccessDeniedException("Esta tarefa não pertence ao projecto informado");
        }
        return task;
    }
    //permite editar
    private void validateTaskEditPermission(Task task,Long projectId, User currentUser) {
        boolean isCreator = task.getCreatedBy().getId().equals(currentUser.getId());
        boolean isOwner = projectMemberRepository.existsByProjectIdAndUserIdAndRole(
                projectId,currentUser.getId(), ProjectRole.Owner);

        if(!isCreator && !isOwner) {
            throw new AccessDeniedException("Apenas o criador da tarefa ou o owner do projeto podem realizar esta ação");
        }
    }

    // resolve o assignedTo a partir do login e valida se é membro do projeto
    private User resolveAssignedTo(Long projectId, String login) {
        if (login == null || login.isBlank()) return null;

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (!projectMemberRepository.existsByProjectIdAndUserId(projectId, user.getId())) {
            throw new IllegalArgumentException("O usuário atribuído não é membro deste projeto");
        }
        return user;
    }



}
