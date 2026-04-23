package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    private TaskService taskService;
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);
        taskController = new TaskController(taskService);
    }

    @Test
    @DisplayName("Should return all tasks")
    void shouldReturnAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task1");

        Task task2 = new Task();
        task2.setTitle("Task2");

        when(taskService.getAllTasks()).thenReturn(List.of(task1, task2));

        List<Task> result = taskController.getAllTasks();

        assertEquals(2, result.size());
        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    @DisplayName("Should return task by id")
    void shouldReturnTaskById() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task1");

        when(taskService.getTaskById(1L)).thenReturn(task);

        Task result = taskController.getTaskById(1L);

        assertEquals("Task1", result.getTitle());
        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    @DisplayName("Should create task")
    void shouldCreateTask() {
        Task task = new Task();
        task.setTitle("NewTask");

        when(taskService.createTask(task)).thenReturn(task);

        Task result = taskController.createTask(task);

        assertEquals("NewTask", result.getTitle());
        verify(taskService, times(1)).createTask(task);
    }

    @Test
    @DisplayName("Should update task")
    void shouldUpdateTask() {
        Task task = new Task();
        task.setTitle("UpdatedTask");

        when(taskService.updateTask(1L, task)).thenReturn(task);

        Task result = taskController.updateTask(1L, task);

        assertEquals("UpdatedTask", result.getTitle());
        verify(taskService, times(1)).updateTask(1L, task);
    }

    @Test
    @DisplayName("Should delete task")
    void shouldDeleteTask() {
        taskController.deleteTask(1L);

        verify(taskService, times(1)).deleteTask(1L);
    }
}