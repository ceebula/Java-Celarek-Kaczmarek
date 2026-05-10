package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        userRepository = mock(UserRepository.class);
        projectService = new ProjectService(projectRepository, userRepository);
    }

    @Test
    @DisplayName("Should return all projects")
    void shouldReturnAllProjects() {
        Project project1 = new Project();
        project1.setName("Project 1");

        Project project2 = new Project();
        project2.setName("Project 2");

        when(projectRepository.findAll()).thenReturn(List.of(project1, project2));

        List<Project> projects = projectService.getAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return project by id")
    void shouldReturnProjectById() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Project 1");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById(1L);

        assertEquals("Project 1", result.getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when project is not found by id")
    void shouldThrowExceptionWhenProjectNotFoundById() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> projectService.getProjectById(1L));
        assertEquals("Project not found", exception.getMessage());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create project")
    void shouldCreateProject() {
        Project project = new Project();
        project.setName("New Project");

        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.createProject(project);

        assertEquals("New Project", result.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should update project")
    void shouldUpdateProject() {
        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("Old Project");
        existingProject.setDescription("Old description");

        Project updatedProject = new Project();
        updatedProject.setName("New Project");
        updatedProject.setDescription("New description");
        updatedProject.setUsers(existingProject.getUsers());

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(existingProject)).thenReturn(existingProject);

        Project result = projectService.updateProject(1L, updatedProject);

        assertEquals("New Project", result.getName());
        assertEquals("New description", result.getDescription());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(existingProject);
    }

    @Test
    @DisplayName("Should throw exception when updated project does not exist")
    void shouldThrowExceptionWhenUpdatedProjectDoesNotExist() {
        Project updatedProject = new Project();
        updatedProject.setName("New Project");

        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> projectService.updateProject(1L, updatedProject));
        assertEquals("Project not found", exception.getMessage());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should assign user to project")
    void shouldAssignUserToProject() {
        Project project = new Project();
        project.setId(1L);

        Users user = new Users();
        user.setId(2L);
        user.setUsername("TestUser");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.assignUserToProject(1L, 2L);

        assertTrue(result.getUsers().contains(user));
        verify(projectRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should throw exception when assigning user to missing project")
    void shouldThrowExceptionWhenAssigningUserToMissingProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> projectService.assignUserToProject(1L, 2L));

        assertEquals("Project not found", exception.getMessage());
        verify(projectRepository, times(1)).findById(1L);
        verify(userRepository, never()).findById(any());
        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when assigned user does not exist")
    void shouldThrowExceptionWhenAssignedUserDoesNotExist() {
        Project project = new Project();
        project.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> projectService.assignUserToProject(1L, 2L));

        assertEquals("User not found", exception.getMessage());
        verify(projectRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete project")
    void shouldDeleteProject() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Project 1");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        projectService.deleteProject(1L);

        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).delete(project);
    }

    @Test
    @DisplayName("Should throw exception when deleted project does not exist")
    void shouldThrowExceptionWhenDeletedProjectDoesNotExist() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> projectService.deleteProject(1L));
        assertEquals("Project not found", exception.getMessage());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, never()).delete(any());
    }
}