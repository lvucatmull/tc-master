package com.tcmaster.dto;

import com.tcmaster.entity.TestSuite;
import jakarta.validation.constraints.NotBlank;

public class SuiteDtos {

    public record SuiteRequest(
            Long parentId,
            @NotBlank String name,
            String description,
            Integer orderIndex
    ) {}

    public record SuiteMoveRequest(
            Long parentId,
            Integer orderIndex
    ) {}

    public record SuiteResponse(
            Long id,
            Long projectId,
            Long parentId,
            String name,
            String description,
            Integer orderIndex
    ) {
        public static SuiteResponse from(TestSuite suite) {
            return new SuiteResponse(
                    suite.getId(),
                    suite.getProject().getId(),
                    suite.getParent() == null ? null : suite.getParent().getId(),
                    suite.getName(),
                    suite.getDescription(),
                    suite.getOrderIndex());
        }
    }
}
