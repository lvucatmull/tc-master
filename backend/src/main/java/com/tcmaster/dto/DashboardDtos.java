package com.tcmaster.dto;

import com.tcmaster.enums.TestResultStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DashboardDtos {

    public record DashboardResponse(
            long totalRuns,
            long totalResults,
            double passRate,
            Map<TestResultStatus, Long> statusCounts,
            List<TrendPoint> trend
    ) {}

    public record TrendPoint(
            LocalDate date,
            long runs,
            long pass,
            long fail
    ) {}

    public record StatsResponse(
            long projects,
            long suites,
            long testCases,
            long automatedCodes
    ) {}

    public record ReportRow(
            String runTitle,
            String tcId,
            String result,
            Integer durationMs,
            String executedAt
    ) {}
}
