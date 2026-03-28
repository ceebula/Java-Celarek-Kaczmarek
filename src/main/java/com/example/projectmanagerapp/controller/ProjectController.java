package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
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
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @PostMapping
    public Project createProject(@RequestBody Project project) {
        return projectRepository.save(project);
    }
}