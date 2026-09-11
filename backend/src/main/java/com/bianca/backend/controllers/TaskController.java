package com.bianca.backend.controllers;

import com.bianca.backend.dtos.TaskDTO;
import com.bianca.backend.dtos.TaskResponse;
import com.bianca.backend.models.Task;
import com.bianca.backend.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<TaskResponse> getTasks(Integer pageNumber, Integer pageSize, String sortBy, String orderDir) {
       return new ResponseEntity<>(taskService.getAllTasks(pageNumber, pageSize,sortBy,orderDir), HttpStatus.OK);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.getTaskById(id),HttpStatus.OK);
    }


    @GetMapping("/tasks/{name]")
    public ResponseEntity<TaskDTO> getTask(@PathVariable String name) {
        return  new ResponseEntity<>(taskService.getTaskByName(name),HttpStatus.OK);
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        return new ResponseEntity<>(taskService.createTask(taskDTO),HttpStatus.CREATED);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        return new ResponseEntity<>(taskService.updateTask(id,taskDTO), HttpStatus.OK);
    }


    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> deleteTask(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.deleteTask(id),HttpStatus.OK);
    }

}
