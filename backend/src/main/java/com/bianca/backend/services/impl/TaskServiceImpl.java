package com.bianca.backend.services.impl;

import com.bianca.backend.dtos.TaskDTO;
import com.bianca.backend.dtos.TaskResponse;
import com.bianca.backend.exception.APIException;
import com.bianca.backend.models.Task;
import com.bianca.backend.repositories.TaskRepository;
import com.bianca.backend.services.TaskService;
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

    @Override
    public TaskResponse getAllTasks(Integer pageNumber, Integer pageSize, String sortBy, String orderDir){

        Sort sortByAndOrder = orderDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Task> taskPage = taskRepository.findAll(pageDetails);

        List<Task> tasks = taskPage.getContent();

        if(tasks.isEmpty()){
            throw new APIException("No tasks created yet.");
        }

        List<TaskDTO> taskDTOS = tasks.stream().map(task -> modelMapper.map(task,TaskDTO.class)).toList();

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setContent(taskDTOS);
        taskResponse.setTotalPages(taskPage.getTotalPages());
        taskResponse.setTotalElements(taskPage.getTotalElements());
        taskResponse.setPageNumber(taskPage.getNumber());
        taskResponse.setPageSize(taskPage.getSize());
        taskResponse.setLastPage(taskPage.isLast());

        return taskResponse;
    }

}
