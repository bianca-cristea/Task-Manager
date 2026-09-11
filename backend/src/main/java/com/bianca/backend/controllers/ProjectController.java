package com.bianca.backend.controllers;

import com.bianca.backend.dtos.ProjectDTO;
import com.bianca.backend.dtos.ProjectResponse;
import com.bianca.backend.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {


    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<ProjectResponse> getAllProjects(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String orderDir
    ) {

        ProjectResponse projectResponse = projectService.getAllProjects(
                pageNumber,
                pageSize,
                sortBy,
                orderDir
        );

        return ResponseEntity.ok(projectResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {

        ProjectDTO projectDTO = projectService.getProjectById(id);

        return ResponseEntity.ok(projectDTO);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProjectDTO> getProjectByName(
            @PathVariable String name
    ) {

        ProjectDTO projectDTO = projectService.getProjectByName(name);

        return ResponseEntity.ok(projectDTO);
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(
            @RequestBody ProjectDTO projectDTO
    ) {

        ProjectDTO createdProject = projectService.createProject(projectDTO);

        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectDTO projectDTO
    ) {

        ProjectDTO updatedProject = projectService.updateProject(id, projectDTO);

        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProjectDTO> deleteProject(@PathVariable Long id) {

        ProjectDTO deletedProject = projectService.deleteProject(id);

        return ResponseEntity.ok(deletedProject);
    }

}
