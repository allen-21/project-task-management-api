package com.muchanga.dev.projecttaskmanagementapi.repository;

import com.muchanga.dev.projecttaskmanagementapi.entity.project.Project;
import com.muchanga.dev.projecttaskmanagementapi.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    List<Project> findAllByMembersUser(User user);
    List<Project> findAllByMembersUserAndNameContainingIgnoreCase(User user, String name);


}
