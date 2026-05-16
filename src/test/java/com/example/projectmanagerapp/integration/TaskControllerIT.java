package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.TaskType;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.TaskRepository;
import com.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.andExpect(status().isOk())
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    private Long userId;
    private Long projectId;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        Users user = new Users();
        user.setUsername("testuser");
        user = userRepository.save(user);
        this.userId = user.getId();

        Project project = new Project();
        project.setName("Test Project");
        project = projectRepository.save(project);
        this.projectId = project.getId();
    }

    @Test
    void shouldCreateTaskSuccessfully() throws Exception {
        String taskJson = """
            {
                "title": "Zadanie Testowe",
                "description": "Opis zadania",
                "taskType": "TASK",
                "project": { "id": %d },
                "owner": { "id": %d }
            }
            """.formatted(projectId, userId);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Zadanie Testowe"))
                .andExpect(jsonPath("$.description").value("Opis zadania"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        createSampleTask();

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Istniejące Zadanie"));
    }

    @Test
    void shouldGetTaskById() throws Exception {
        Task savedTask = createSampleTask();

        mockMvc.perform(get("/api/tasks/{id}", savedTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTask.getId()))
                .andExpect(jsonPath("$.title").value("Istniejące Zadanie"));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        Task savedTask = createSampleTask();
        String updatedJson = """
            {
                "title": "Zaktualizowany Tytuł",
                "description": "Nowy opis",
                "taskType": "BUG",
                "project": { "id": %d },
                "owner": { "id": %d }
            }
            """.formatted(projectId, userId);

        mockMvc.perform(put("/api/tasks/{id}", savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Zaktualizowany Tytuł"))
                .andExpect(jsonPath("$.taskType").value("BUG"));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        Task savedTask = createSampleTask();

        mockMvc.perform(delete("/api/tasks/{id}", savedTask.getId()))
                .andExpect(status().isOk());

        Optional<Task> deletedTask = taskRepository.findById(savedTask.getId());
        assertFalse(deletedTask.isPresent());
    }

    private Task createSampleTask() {
        Task task = new Task();
        task.setTitle("Istniejące Zadanie");
        task.setDescription("Opis");
        task.setTaskType(TaskType.TASK);

        Project p = projectRepository.findById(projectId).orElseThrow();
        Users u = userRepository.findById(userId).orElseThrow();

        task.setProject(p);
        task.setOwner(u);
        return taskRepository.save(task);
    }
}