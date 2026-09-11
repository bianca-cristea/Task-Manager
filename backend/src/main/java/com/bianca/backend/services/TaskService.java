package com.bianca.backend.services;

import com.bianca.backend.dtos.TaskDTO;
import com.bianca.backend.dtos.TaskResponse;

public interface TaskService {

    public TaskResponse getAllTasks(Integer pageNumber, Integer pageSize, String sortBy, String orderDir);
    public TaskDTO getTaskById(Long id);
    public TaskDTO getTaskByName(String name);

    public TaskDTO createTask(TaskDTO taskDTO);

    public TaskDTO updateTask(Long id, TaskDTO taskDTO);
    public TaskDTO deleteTask(Long id);
    }
