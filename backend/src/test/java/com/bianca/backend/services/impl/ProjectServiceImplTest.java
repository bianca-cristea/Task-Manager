package com.bianca.backend.services.impl;

import com.bianca.backend.dtos.ProjectDTO;
import com.bianca.backend.exception.APIException;
import com.bianca.backend.exception.ResourceNotFoundException;
import com.bianca.backend.models.Project;
import com.bianca.backend.models.User;
import com.bianca.backend.repositories.ProjectRepository;
import com.bianca.backend.util.AuthUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthUtil authUtil;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    void createProject_shouldSetLoggedInUserAsOwner() {

        User loggedInUser = new User();
        loggedInUser.setUserId(1L);

        ProjectDTO inputDto = new ProjectDTO();
        inputDto.setProjectName("New Project");
        inputDto.setDescription("Description");


        when(authUtil.loggedInUser()).thenReturn(loggedInUser);
        when(projectRepository.save(org.mockito.ArgumentMatchers.any(Project.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProjectDTO outputDto = new ProjectDTO();
        outputDto.setProjectName("New Project");
        when(modelMapper.map(org.mockito.ArgumentMatchers.any(Project.class), org.mockito.ArgumentMatchers.eq(ProjectDTO.class)))
                .thenReturn(outputDto);


        ProjectDTO result = projectService.createProject(inputDto);


        assertEquals("New Project", result.getProjectName());

        org.mockito.Mockito.verify(authUtil).loggedInUser();
    }

    @Test
    void updateProject_shouldThrowException_whenUserIsNotOwner() {

        User owner = new User();
        owner.setUserId(1L);

        Project existingProject = new Project();
        existingProject.setProjectId(10L);
        existingProject.setUser(owner);

        when(projectRepository.findById(10L)).thenReturn(Optional.of(existingProject));
        when(authUtil.loggedInUserId()).thenReturn(2L);

        ProjectDTO updateDto = new ProjectDTO();
        updateDto.setProjectName("Hacked name");


        assertThrows(APIException.class, () -> {
            projectService.updateProject(10L, updateDto);
        });
    }

    @Test
    void getProjectById_shouldThrowException_whenNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            projectService.getProjectById(99L);
        });
    }

}