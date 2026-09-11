package com.bianca.backend.services;

import com.bianca.backend.dtos.TaskResponse;

public interface TaskService {

    public TaskResponse getAllTasks(Integer pageNumber, Integer pageSize, String sortBy, String orderDir);

    }
