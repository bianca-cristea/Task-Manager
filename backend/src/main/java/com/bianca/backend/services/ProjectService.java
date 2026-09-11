package com.bianca.backend.services;

import com.bianca.backend.dtos.ProjectDTO;
import com.bianca.backend.dtos.ProjectResponse;

public interface ProjectService {
    public ProjectResponse getAllProjects(Integer pageNumber, Integer pageSize, String sortBy, String orderDir);

    public ProjectDTO getProjectById(Long id);
    public ProjectDTO getProjectByName(String name);
    public ProjectDTO createProject(ProjectDTO projectDTO);
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO);
    public ProjectDTO deleteProject(Long id);
}
