package com.bianca.backend.services.impl;

import com.bianca.backend.dtos.ProjectDTO;
import com.bianca.backend.dtos.ProjectResponse;
import com.bianca.backend.exception.APIException;
import com.bianca.backend.exception.ResourceNotFoundException;
import com.bianca.backend.models.Project;
import com.bianca.backend.repositories.ProjectRepository;
import com.bianca.backend.services.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProjectResponse getAllProjects(Integer pageNumber, Integer pageSize, String sortBy, String orderDir) {
        Sort sortByAndOrder = orderDir.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Pageable pageRequest = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Project> projectPage = projectRepository.findAll(pageRequest);

        List<Project> projectList = projectPage.getContent();
        if(projectList.isEmpty()) throw new APIException("No projects found");

        List<ProjectDTO> projectDTOS = projectList.stream().map(project -> modelMapper.map(project,ProjectDTO.class)).toList();

        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setContent(projectDTOS);
        projectResponse.setPageNumber(projectPage.getNumber());
        projectResponse.setPageSize(projectPage.getSize());
        projectResponse.setTotalElements(projectPage.getTotalElements());
        projectResponse.setTotalPages(projectPage.getTotalPages());

        return projectResponse;
    }

    @Override
    public ProjectDTO getProjectById(Long id) {

        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project","projectId",id));
        return  modelMapper.map(project,ProjectDTO.class);

    }

    @Override
    public ProjectDTO getProjectByName(String name) {
        Project project = projectRepository.findByProjectName(name).orElseThrow(() -> new ResourceNotFoundException("Project","ProjetName",name));

        return  modelMapper.map(project,ProjectDTO.class);
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setProjectName(projectDTO.getProjectName());
        project.setDescription(projectDTO.getDescription());

        return modelMapper.map(projectRepository.save(project),ProjectDTO.class);
    }

    @Override
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project","projectId",id));

        project.setProjectName(projectDTO.getProjectName());
        project.setDescription(projectDTO.getDescription());
        projectRepository.save(project);

        return modelMapper.map(project,ProjectDTO.class);
    }

    @Override
    public ProjectDTO deleteProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project","projectId",id));
        projectRepository.delete(project);

        return modelMapper.map(project,ProjectDTO.class);
    }
}
