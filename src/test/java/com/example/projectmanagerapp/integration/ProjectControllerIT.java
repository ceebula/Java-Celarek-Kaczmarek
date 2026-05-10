package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.TaskRepository;
import com.example.projectmanagerapp.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("integration-test")
class ProjectControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateProject() throws Exception {
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Project A",
                                  "description": "Description A"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Project A"))
                .andExpect(jsonPath("$.description").value("Description A"));
    }

    @Test
    void shouldGetAllProjects() throws Exception {
        createProject("Project A", "Description A");
        createProject("Project B", "Description B");

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldGetProjectById() throws Exception {
        long projectId = createProject("Project A", "Description A");

        mockMvc.perform(get("/api/projects/{id}", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectId))
                .andExpect(jsonPath("$.name").value("Project A"))
                .andExpect(jsonPath("$.description").value("Description A"));
    }

    @Test
    void shouldUpdateProject() throws Exception {
        long projectId = createProject("Project A", "Description A");

        mockMvc.perform(put("/api/projects/{id}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Updated Project",
                                  "description": "Updated Description"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectId))
                .andExpect(jsonPath("$.name").value("Updated Project"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    void shouldDeleteProject() throws Exception {
        long projectId = createProject("Project A", "Description A");

        mockMvc.perform(delete("/api/projects/{id}", projectId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldAssignUserToProject() throws Exception {
        long userId = createUser("studentA");
        long projectId = createProject("Project A", "Description A");

        mockMvc.perform(post("/api/projects/{projectId}/users", projectId)
                        .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users.length()").value(1))
                .andExpect(jsonPath("$.users[0].id").value(userId))
                .andExpect(jsonPath("$.users[0].username").value("studentA"));
    }

    private long createProject(String name, String description) throws Exception {
        String response = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "%s",
                                  "description": "%s"
                                }
                                """.formatted(name, description)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        return json.get("id").asLong();
    }

    private long createUser(String username) throws Exception {
        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "%s"
                                }
                                """.formatted(username)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        return json.get("id").asLong();
    }
}