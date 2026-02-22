package com.tcmaster.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.tcmaster.dto.ProjectDtos;
import com.tcmaster.entity.Project;
import com.tcmaster.entity.User;
import com.tcmaster.repository.ProjectRepository;
import com.tcmaster.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;

    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectService = new ProjectService(projectRepository, userRepository);
    }

    @Test
    void create_uppercasesProjectCode() {
        User user = new User();
        user.setEmail("owner@example.com");

        when(userRepository.findByEmail("owner@example.com")).thenReturn(Optional.of(user));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProjectDtos.ProjectResponse response = projectService.create(
                new ProjectDtos.ProjectRequest("My Project", "tcm", "desc"),
                "owner@example.com"
        );

        assertEquals("TCM", response.code());
    }
}
