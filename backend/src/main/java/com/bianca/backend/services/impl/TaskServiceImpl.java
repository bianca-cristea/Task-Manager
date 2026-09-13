package com.bianca.backend.services.impl;

import com.bianca.backend.dtos.TaskDTO;
import com.bianca.backend.dtos.TaskResponse;
import com.bianca.backend.exception.APIException;
import com.bianca.backend.models.Task;
import com.bianca.backend.repositories.CategoryRepository;
import com.bianca.backend.repositories.ProjectRepository;
import com.bianca.backend.repositories.TaskRepository;
import com.bianca.backend.services.TaskService;
import com.bianca.backend.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthUtil authUtil;


    @Override
    public TaskResponse getAllTasksForAdmin(Integer pageNumber, Integer pageSize, String sortBy, String orderDir) {
        Sort sortByAndOrder = orderDir.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Pageable pageRequest = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Task> taskPage = taskRepository.findAll(pageRequest);

        List<Task> tasks = taskPage.getContent();

        if(tasks.isEmpty()) throw new APIException("No tasks found");

        List<TaskDTO> taskDTOS = tasks.stream().map(task -> modelMapper.map(task, TaskDTO.class)).toList();

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setContent(taskDTOS);
        taskResponse.setPageNumber(taskPage.getNumber());
        taskResponse.setPageSize(taskPage.getSize());
        taskResponse.setTotalPages(taskPage.getTotalPages());
        taskResponse.setTotalElements(taskPage.getTotalElements());
        taskResponse.setLastPage(taskPage.isLast());

        return taskResponse;
    }

    @Override
    public TaskResponse getAllTasksForUser(Integer pageNumber, Integer pageSize, String sortBy, String orderDir) {
        Sort sortByAndOrder = orderDir.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Pageable pageRequest = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Task> taskPage = taskRepository.findByProject_User(authUtil.loggedInUser(), pageRequest);

        List<Task> tasks = taskPage.getContent();
        if(tasks.isEmpty()) throw new APIException("No tasks found");

        List<TaskDTO>  taskDTOS = tasks.stream().map(task -> modelMapper.map(task, TaskDTO.class)).toList();

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setContent(taskDTOS);
        taskResponse.setPageNumber(taskPage.getNumber());
        taskResponse.setPageSize(taskPage.getSize());
        taskResponse.setTotalPages(taskPage.getTotalPages());
        taskResponse.setTotalElements(taskPage.getTotalElements());
        taskResponse.setLastPage(taskPage.isLast());

        return taskResponse;
    }

    @Override
    public TaskDTO getTaskById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new APIException("Task not found with id: " + id));

        return modelMapper.map(task, TaskDTO.class);
    }

    @Override
    public TaskDTO getTaskByName(String name) {

        Task task = taskRepository.findByTitle(name)
                .orElseThrow(() -> new APIException("Task not found with name: " + name));

        return modelMapper.map(task, TaskDTO.class);
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {

        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setCategory(categoryRepository.findById(taskDTO.getCategoryId()).orElseThrow(() -> new APIException("Category not found with id: " + taskDTO.getCategoryId())));
        task.setProject(projectRepository.findById(taskDTO.getProjectId()).orElseThrow(() -> new APIException("Project not found with id: "+ + taskDTO.getCategoryId())));

        Task savedTask = taskRepository.save(task);

        return modelMapper.map(savedTask, TaskDTO.class);
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new APIException(
                        "Task not found with id: " + taskDTO.getTaskId()
                ));

        modelMapper.map(taskDTO, task);

        Task updatedTask = taskRepository.save(task);

        return modelMapper.map(updatedTask, TaskDTO.class);
    }

    @Override
    public TaskDTO deleteTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new APIException(
                        "Task not found with id: " + id
                ));

        taskRepository.delete(task);

        return modelMapper.map(task, TaskDTO.class);
    }
}
