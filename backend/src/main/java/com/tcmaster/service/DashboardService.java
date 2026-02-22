package com.tcmaster.service;

import com.tcmaster.dto.DashboardDtos;
import com.tcmaster.entity.TestResult;
import com.tcmaster.entity.TestRun;
import com.tcmaster.enums.TestResultStatus;
import com.tcmaster.repository.ProjectRepository;
import com.tcmaster.repository.TestCaseRepository;
import com.tcmaster.repository.TestCodeRepository;
import com.tcmaster.repository.TestResultRepository;
import com.tcmaster.repository.TestRunRepository;
import com.tcmaster.repository.TestSuiteRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final TestRunRepository testRunRepository;
    private final TestResultRepository testResultRepository;
    private final ProjectRepository projectRepository;
    private final TestSuiteRepository testSuiteRepository;
    private final TestCaseRepository testCaseRepository;
    private final TestCodeRepository testCodeRepository;

    public DashboardService(
            TestRunRepository testRunRepository,
            TestResultRepository testResultRepository,
            ProjectRepository projectRepository,
            TestSuiteRepository testSuiteRepository,
            TestCaseRepository testCaseRepository,
            TestCodeRepository testCodeRepository) {
        this.testRunRepository = testRunRepository;
        this.testResultRepository = testResultRepository;
        this.projectRepository = projectRepository;
        this.testSuiteRepository = testSuiteRepository;
        this.testCaseRepository = testCaseRepository;
        this.testCodeRepository = testCodeRepository;
    }

    public DashboardDtos.DashboardResponse getDashboard(Long projectId) {
        List<TestRun> runs = testRunRepository.findByProjectIdOrderByIdDesc(projectId);
        List<TestResult> results = runs.stream()
                .flatMap(run -> testResultRepository.findByTestRunIdOrderByIdAsc(run.getId()).stream())
                .toList();

        Map<TestResultStatus, Long> statusCounts = new EnumMap<>(TestResultStatus.class);
        for (TestResultStatus status : TestResultStatus.values()) {
            statusCounts.put(status, 0L);
        }
        for (TestResult result : results) {
            statusCounts.computeIfPresent(result.getResult(), (k, v) -> v + 1);
        }

        long passCount = statusCounts.getOrDefault(TestResultStatus.PASS, 0L);
        double passRate = results.isEmpty() ? 0.0 : (passCount * 100.0) / results.size();

        Map<LocalDate, List<TestResult>> byDate = results.stream()
                .collect(Collectors.groupingBy(r -> r.getExecutedAt().toLocalDate()));

        List<DashboardDtos.TrendPoint> trend = byDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    long pass = entry.getValue().stream().filter(r -> r.getResult() == TestResultStatus.PASS).count();
                    long fail = entry.getValue().stream().filter(r -> r.getResult() == TestResultStatus.FAIL).count();
                    return new DashboardDtos.TrendPoint(entry.getKey(), entry.getValue().size(), pass, fail);
                })
                .toList();

        return new DashboardDtos.DashboardResponse(runs.size(), results.size(), passRate, statusCounts, trend);
    }

    public DashboardDtos.StatsResponse getStats(Long projectId) {
        long suites = testSuiteRepository.findByProjectIdOrderByOrderIndexAscIdAsc(projectId).size();
        long testCases = testCaseRepository.findAll().stream()
                .filter(c -> c.getSuite().getProject().getId().equals(projectId))
                .count();
        long codes = testCodeRepository.findAll().stream()
                .filter(c -> c.getTestCase().getSuite().getProject().getId().equals(projectId))
                .count();
        return new DashboardDtos.StatsResponse(projectRepository.count(), suites, testCases, codes);
    }

    public List<DashboardDtos.ReportRow> getReports(Long projectId) {
        List<DashboardDtos.ReportRow> rows = new ArrayList<>();
        List<TestRun> runs = testRunRepository.findByProjectIdOrderByIdDesc(projectId);

        for (TestRun run : runs) {
            List<TestResult> results = testResultRepository.findByTestRunIdOrderByIdAsc(run.getId());
            for (TestResult result : results) {
                rows.add(new DashboardDtos.ReportRow(
                        run.getTitle(),
                        result.getTestCase().getTcId(),
                        result.getResult().name(),
                        result.getDurationMs(),
                        result.getExecutedAt().toString()));
            }
        }

        return rows;
    }

    public String exportCsv(Long projectId) {
        List<DashboardDtos.ReportRow> rows = getReports(projectId);
        StringBuilder sb = new StringBuilder();
        sb.append("runTitle,tcId,result,durationMs,executedAt\n");
        for (DashboardDtos.ReportRow row : rows) {
            sb.append(sanitize(row.runTitle())).append(',')
                    .append(sanitize(row.tcId())).append(',')
                    .append(row.result()).append(',')
                    .append(row.durationMs()).append(',')
                    .append(row.executedAt()).append('\n');
        }
        return sb.toString();
    }

    private String sanitize(String value) {
        return '"' + value.replace("\"", "\"\"") + '"';
    }
}
