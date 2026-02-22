package com.tcmaster.dto;

import com.tcmaster.entity.TestRun;
import com.tcmaster.entity.TestResult;
import com.tcmaster.enums.TestResultStatus;
import com.tcmaster.enums.TestRunStatus;
import com.tcmaster.enums.TestRunType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class RunDtos {

    public record RunCreateRequest(
            @NotBlank String title,
            @NotNull TestRunType runType
    ) {}

    public record RunResponse(
            Long id,
            Long projectId,
            String title,
            TestRunType runType,
            TestRunStatus status,
            LocalDateTime startedAt,
            LocalDateTime completedAt
    ) {
        public static RunResponse from(TestRun run) {
            return new RunResponse(
                    run.getId(),
                    run.getProject().getId(),
                    run.getTitle(),
                    run.getRunType(),
                    run.getStatus(),
                    run.getStartedAt(),
                    run.getCompletedAt());
        }
    }

    public record ResultResponse(
            Long id,
            Long testRunId,
            Long testCaseId,
            TestResultStatus result,
            Integer durationMs,
            String logOutput,
            String errorMessage,
            LocalDateTime executedAt
    ) {
        public static ResultResponse from(TestResult result) {
            return new ResultResponse(
                    result.getId(),
                    result.getTestRun().getId(),
                    result.getTestCase().getId(),
                    result.getResult(),
                    result.getDurationMs(),
                    result.getLogOutput(),
                    result.getErrorMessage(),
                    result.getExecutedAt());
        }
    }

    public record RunEvent(
            Long runId,
            String type,
            String message,
            TestRunStatus status,
            LocalDateTime timestamp
    ) {}
}
