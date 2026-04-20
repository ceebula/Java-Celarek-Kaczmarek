package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**Kontroler zarządzający projektami w systemie.
 GET  http://localhost:8080/api/projects   - Pobiera listę wszystkich projektów
 POST http://localhost:8080/api/projects   - Tworzy nowy projekt
 {
 "name": "System Symulacji Ewolucyjnej",
 "description": "Projekt zaliczeniowy z Programowania Obiektowego",
 "startDate": "2026-03-01",
 "status": "ACTIVE"
 }
 */

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operations for managing projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Returns a list of all projects")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by id", description = "Returns project data for the given id")
    public Project getProjectById(@Parameter(description = "Project ID") @PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PostMapping
    @Operation(summary = "Create project", description = "Creates a new project")
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project", description = "Updates an existing project for the given id")
    public Project updateProject(@Parameter(description = "Project ID") @PathVariable Long id, @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project", description = "Deletes a project for the given id")
    public void deleteProject(@Parameter(description = "Project ID") @PathVariable Long id) {
        projectService.deleteProject(id);
    }
}