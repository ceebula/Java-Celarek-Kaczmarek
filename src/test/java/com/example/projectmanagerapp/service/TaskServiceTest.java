package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.TaskType;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    void shouldReturnAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setTitle("Task 2");

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by id")
    void shouldReturnTaskById() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertEquals("Task 1", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when task is not found by id")
    void shouldThrowExceptionWhenTaskNotFoundById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskService.getTaskById(1L));
        assertEquals("Task not found", exception.getMessage());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create task")
    void shouldCreateTask() {
        Task task = new Task();
        task.setTitle("New Task");

        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task);

        assertEquals("New Task", result.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should update task")
    void shouldUpdateTask() {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old Task");
        existingTask.setDescription("Old description");
        existingTask.setTaskType(TaskType.TASK);

        Task updatedTask = new Task();
        updatedTask.setTitle("New Task");
        updatedTask.setDescription("New description");
        updatedTask.setTaskType(TaskType.BUG);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertEquals("New Task", result.getTitle());
        assertEquals("New description", result.getDescription());
        assertEquals(TaskType.BUG, result.getTaskType());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    @DisplayName("Should throw exception when updated task does not exist")
    void shouldThrowExceptionWhenUpdatedTaskDoesNotExist() {
        Task updatedTask = new Task();
        updatedTask.setTitle("New Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskService.updateTask(1L, updatedTask));
        assertEquals("Task not found", exception.getMessage());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete task")
    void shouldDeleteTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    @DisplayName("Should throw exception when deleted task does not exist")
    void shouldThrowExceptionWhenDeletedTaskDoesNotExist() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskService.deleteTask(1L));
        assertEquals("Task not found", exception.getMessage());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).delete(any());
    }
}