package com.bianca.backend.controllers;

import com.bianca.backend.config.AppConstants;
import com.bianca.backend.dtos.TaskDTO;
import com.bianca.backend.dtos.TaskResponse;
import com.bianca.backend.models.Task;
import com.bianca.backend.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/tasks")
    public ResponseEntity<TaskResponse> getTasksForAdmin(
            @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_TASKS_BY, required = false) String sortBy,
            @RequestParam(name="orderDir", defaultValue = AppConstants.SORT_DIR, required = false) String orderDir
    ){
        return new ResponseEntity<>(taskService.getAllTasksForAdmin(pageNumber,pageSize,sortBy,orderDir),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my/tasks")
    public ResponseEntity<TaskResponse> getTasksForUser(
            @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_TASKS_BY, required = false) String sortBy,
            @RequestParam(name="orderDir", defaultValue = AppConstants.SORT_DIR, required = false) String orderDir
    ){
        return new ResponseEntity<>(taskService.getAllTasksForUser(pageNumber,pageSize,sortBy,orderDir),HttpStatus.OK);
    }
    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.getTaskById(id),HttpStatus.OK);
    }


    @GetMapping("/tasks/title/{title}")
    public ResponseEntity<TaskDTO> getTaskByTitle(@PathVariable String title) {
        return  new ResponseEntity<>(taskService.getTaskByName(title),HttpStatus.OK);
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
