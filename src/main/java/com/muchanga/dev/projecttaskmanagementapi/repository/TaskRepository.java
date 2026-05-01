package com.muchanga.dev.projecttaskmanagementapi.repository;

import com.muchanga.dev.projecttaskmanagementapi.entity.task.Task;
import com.muchanga.dev.projecttaskmanagementapi.entity.task.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    // todas as tarefas de um projeto
    List<Task> findAllByProjectId(Long projectId);

    // tarefas atribuídas a um usuário dentro de um projeto
    List<Task> findAllByProjectIdAndAssignedToId(Long projectId, Long assignedToId);

    // tarefas por status dentro de um projeto
    List<Task> findAllByProjectIdAndStatus(Long projectId, TaskStatus status);

    List<Task> findAllByProjectIdAndTitleContainingIgnoreCase(Long projectId, String title);

    void deleteAllByProjectId(Long projectId);
}
