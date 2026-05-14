package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Users;
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

// Importy statyczne warto trzymać w jednym bloku
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("integration-test")
class TaskControllerIT {

    private MockMvc mockMvc;

    // Lepiej użyć ObjectMapper zarządzanego przez Springa, jeśli to możliwe,
    // ale Twoje rozwiązanie z 'new' jest bezpieczne przy problemach z kontekstem.
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        // TUTAJ: Możesz dodać czyszczenie bazy danych przez repozytoria
    }

    // Dodaj faktyczny test, bo samo contextLoads() sprawdza tylko czy aplikacja startuje
    @Test
    void shouldCreateTaskSuccessfully() throws Exception {
        Long userId = createUser();
        Long projectId = createProject();

        // Tutaj dopisz logikę tworzenia zadania (Task) używając userId i projectId
    }

    private Long createUser() throws Exception {
        Users user = new Users();
        user.setUsername("user_" + System.currentTimeMillis()); // Unikalny username dla każdego testu

        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    private Long createProject() throws Exception {
        Project project = new Project();
        project.setName("Project " + System.currentTimeMillis());
        project.setDescription("Integration tests");

        String response = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    void contextLoads() {
    }
}