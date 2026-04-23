package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProjectControllerTest {

    private ProjectService projectService;
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        projectService = mock(ProjectService.class);
        projectController = new ProjectController(projectService);
    }

    @Test
    @DisplayName("Should return all projects")
    void shouldReturnAllProjects() {
        Project project1 = new Project();
        project1.setName("Project1");

        Project project2 = new Project();
        project2.setName("Project2");

        when(projectService.getAllProjects()).thenReturn(List.of(project1, project2));

        List<Project> result = projectController.getAllProjects();

        assertEquals(2, result.size());
        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    @DisplayName("Should return project by id")
    void shouldReturnProjectById() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Project1");

        when(projectService.getProjectById(1L)).thenReturn(project);

        Project result = projectController.getProjectById(1L);

        assertEquals("Project1", result.getName());
        verify(projectService, times(1)).getProjectById(1L);
    }

    @Test
    @DisplayName("Should create project")
    void shouldCreateProject() {
        Project project = new Project();
        project.setName("NewProject");

        when(projectService.createProject(project)).thenReturn(project);

        Project result = projectController.createProject(project);

        assertEquals("NewProject", result.getName());
        verify(projectService, times(1)).createProject(project);
    }

    @Test
    @DisplayName("Should update project")
    void shouldUpdateProject() {
        Project project = new Project();
        project.setName("UpdatedProject");

        when(projectService.updateProject(1L, project)).thenReturn(project);

        Project result = projectController.updateProject(1L, project);

        assertEquals("UpdatedProject", result.getName());
        verify(projectService, times(1)).updateProject(1L, project);
    }

    @Test
    @DisplayName("Should delete project")
    void shouldDeleteProject() {
        projectController.deleteProject(1L);

        verify(projectService, times(1)).deleteProject(1L);
    }
}