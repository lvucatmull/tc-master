package com.tcmaster.dto;

import com.tcmaster.entity.Project;
import jakarta.validation.constraints.NotBlank;

public class ProjectDtos {

    public record ProjectRequest(
            @NotBlank String name,
            @NotBlank String code,
            String description
    ) {}

    public record ProjectResponse(
            Long id,
            String name,
            String code,
            String description
    ) {
        public static ProjectResponse from(Project project) {
            return new ProjectResponse(
                    project.getId(),
                    project.getName(),
                    project.getCode(),
                    project.getDescription()
            );
        }
    }
}
