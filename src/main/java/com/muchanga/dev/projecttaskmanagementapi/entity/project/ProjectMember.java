package com.muchanga.dev.projecttaskmanagementapi.entity.project;

import com.muchanga.dev.projecttaskmanagementapi.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "user_id"}))
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectRole role;

    public ProjectMember(Project project, User user, ProjectRole role) {
        this.project = project;
        this.user = user;
        this.role = role;
    }
}
