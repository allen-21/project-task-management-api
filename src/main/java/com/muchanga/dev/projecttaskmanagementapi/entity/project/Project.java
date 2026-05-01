package com.muchanga.dev.projecttaskmanagementapi.entity.project;

import com.muchanga.dev.projecttaskmanagementapi.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "project")
    private List<ProjectMember> members = new ArrayList<>();

    public Project(String name, String description, User creator) {
        this.name = name;
        this.description = description;
        this.creator = creator;
    }

}
