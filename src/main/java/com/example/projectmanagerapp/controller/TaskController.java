package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
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
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }
}