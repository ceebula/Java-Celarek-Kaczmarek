package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 GET  http://localhost:8080/api/tasks   - Pobiera wszystkie zadania
 POST http://localhost:8080/api/tasks   - Tworzy nowe zadanie
 {
 "title": "Zaimplementować logowanie",
 "description": "Dodać Spring Security i JWT",
 "status": "IN_PROGRESS",
 "priority": 1,
 "dueDate": "2026-04-15T12:00:00"
 }
 */

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Operations for managing tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Returns a list of all tasks")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by id", description = "Returns task data for the given id")
    public Task getTaskById(@Parameter(description = "Task ID") @PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task", description = "Updates an existing task for the given id")
    public Task updateTask(@Parameter(description = "Task ID") @PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Deletes a task for the given id")
    public void deleteTask(@Parameter(description = "Task ID") @PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @PostMapping
    @Operation(summary = "Create task", description = "Creates a new task")
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }
}