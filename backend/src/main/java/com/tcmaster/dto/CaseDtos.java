package com.tcmaster.dto;

import com.tcmaster.entity.TestCase;
import com.tcmaster.enums.TestCasePriority;
import com.tcmaster.enums.TestCaseStatus;
import jakarta.validation.constraints.NotBlank;

public class CaseDtos {

    public record CaseRequest(
            @NotBlank String title,
            String description,
            String precondition,
            String steps,
            String expectedResult,
            TestCasePriority priority,
            String category,
            String tags,
            TestCaseStatus status
    ) {}

    public record CaseResponse(
            Long id,
            String tcId,
            Long suiteId,
            String title,
            String description,
            String precondition,
            String steps,
            String expectedResult,
            TestCasePriority priority,
            String category,
            String tags,
            TestCaseStatus status
    ) {
        public static CaseResponse from(TestCase testCase) {
            return new CaseResponse(
                    testCase.getId(),
                    testCase.getTcId(),
                    testCase.getSuite().getId(),
                    testCase.getTitle(),
                    testCase.getDescription(),
                    testCase.getPrecondition(),
                    testCase.getSteps(),
                    testCase.getExpectedResult(),
                    testCase.getPriority(),
                    testCase.getCategory(),
                    testCase.getTags(),
                    testCase.getStatus());
        }
    }
}
