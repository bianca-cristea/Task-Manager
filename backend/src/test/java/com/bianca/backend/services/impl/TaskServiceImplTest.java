package com.bianca.backend.services.impl;

import com.bianca.backend.dtos.TaskDTO;
import com.bianca.backend.exception.APIException;
import com.bianca.backend.models.Category;
import com.bianca.backend.models.Project;
import com.bianca.backend.models.Task;
import com.bianca.backend.repositories.CategoryRepository;
import com.bianca.backend.repositories.ProjectRepository;
import com.bianca.backend.repositories.TaskRepository;
import com.bianca.backend.util.AuthUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AuthUtil authUtil;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void createTask_shouldThrowException_whenCategoryNotFound() {

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setCategoryId(99L);
        taskDTO.setProjectId(1L);

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());


        assertThrows(APIException.class, () -> {
            taskService.createTask(taskDTO);
        });
    }

    @Test
    void createTask_shouldSaveTask_whenCategoryAndProjectExist() {

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("New Task");
        taskDTO.setCategoryId(1L);
        taskDTO.setProjectId(1L);

        Category category = new Category();
        category.setCategoryId(1L);

        Project project = new Project();
        project.setProjectId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskDTO outputDto = new TaskDTO();
        outputDto.setTitle("New Task");
        when(modelMapper.map(any(Task.class), org.mockito.ArgumentMatchers.eq(TaskDTO.class))).thenReturn(outputDto);


        TaskDTO result = taskService.createTask(taskDTO);


        assertEquals("New Task", result.getTitle());
    }

    @Test
    void getTaskById_shouldThrowException_whenNotFound() {
        when(taskRepository.findById(50L)).thenReturn(Optional.empty());

        assertThrows(APIException.class, () -> {
            taskService.getTaskById(50L);
        });
    }


}