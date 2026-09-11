package com.bianca.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "projects")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @NotEmpty(message = "Project name should not be empty.")
    private String projectName;

    @NotEmpty(message = "Description should not be empty.")
    private String description;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

}
