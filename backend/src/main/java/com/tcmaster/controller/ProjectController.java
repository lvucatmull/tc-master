package com.tcmaster.controller;

import com.tcmaster.dto.ProjectDtos;
import com.tcmaster.service.ProjectService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectDtos.ProjectResponse> getProjects() {
        return projectService.getProjects();
    }

    @PostMapping
    public ProjectDtos.ProjectResponse createProject(@Valid @RequestBody ProjectDtos.ProjectRequest request, Principal principal) {
        return projectService.create(request, principal.getName());
    }

    @GetMapping("/{id}")
    public ProjectDtos.ProjectResponse getProject(@PathVariable Long id) {
        return projectService.getById(id);
    }

    @PutMapping("/{id}")
    public ProjectDtos.ProjectResponse updateProject(@PathVariable Long id, @Valid @RequestBody ProjectDtos.ProjectRequest request) {
        return projectService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.delete(id);
    }
}
