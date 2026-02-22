package com.tcmaster.service;

import com.tcmaster.dto.ProjectDtos;
import com.tcmaster.entity.Project;
import com.tcmaster.entity.User;
import com.tcmaster.repository.ProjectRepository;
import com.tcmaster.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<ProjectDtos.ProjectResponse> getProjects() {
        return projectRepository.findAll().stream().map(ProjectDtos.ProjectResponse::from).toList();
    }

    public ProjectDtos.ProjectResponse create(ProjectDtos.ProjectRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Project project = new Project();
        project.setName(request.name());
        project.setCode(request.code().toUpperCase());
        project.setDescription(request.description());
        project.setCreatedBy(user);
        return ProjectDtos.ProjectResponse.from(projectRepository.save(project));
    }

    public ProjectDtos.ProjectResponse update(Long id, ProjectDtos.ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        project.setName(request.name());
        project.setCode(request.code().toUpperCase());
        project.setDescription(request.description());
        return ProjectDtos.ProjectResponse.from(projectRepository.save(project));
    }

    public ProjectDtos.ProjectResponse getById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        return ProjectDtos.ProjectResponse.from(project);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
