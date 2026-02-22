package com.tcmaster.entity;

import com.tcmaster.enums.TestRunStatus;
import com.tcmaster.enums.TestRunType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_runs")
public class TestRun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestRunType runType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestRunStatus status = TestRunStatus.PENDING;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "triggered_by", nullable = false)
    private User triggeredBy;

    public Long getId() { return id; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public TestRunType getRunType() { return runType; }
    public void setRunType(TestRunType runType) { this.runType = runType; }
    public TestRunStatus getStatus() { return status; }
    public void setStatus(TestRunStatus status) { this.status = status; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public User getTriggeredBy() { return triggeredBy; }
    public void setTriggeredBy(User triggeredBy) { this.triggeredBy = triggeredBy; }
}
