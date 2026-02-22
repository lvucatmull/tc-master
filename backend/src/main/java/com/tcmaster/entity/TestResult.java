package com.tcmaster.entity;

import com.tcmaster.enums.TestResultStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_results")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_run_id", nullable = false)
    private TestRun testRun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_case_id", nullable = false)
    private TestCase testCase;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestResultStatus result;

    @Column(nullable = false)
    private Integer durationMs;

    @Column(columnDefinition = "TEXT")
    private String logOutput;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime executedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public TestRun getTestRun() { return testRun; }
    public void setTestRun(TestRun testRun) { this.testRun = testRun; }
    public TestCase getTestCase() { return testCase; }
    public void setTestCase(TestCase testCase) { this.testCase = testCase; }
    public TestResultStatus getResult() { return result; }
    public void setResult(TestResultStatus result) { this.result = result; }
    public Integer getDurationMs() { return durationMs; }
    public void setDurationMs(Integer durationMs) { this.durationMs = durationMs; }
    public String getLogOutput() { return logOutput; }
    public void setLogOutput(String logOutput) { this.logOutput = logOutput; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public LocalDateTime getExecutedAt() { return executedAt; }
}
