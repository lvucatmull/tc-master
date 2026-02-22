package com.tcmaster.service;

import com.tcmaster.dto.RunDtos;
import com.tcmaster.entity.Project;
import com.tcmaster.entity.TestCase;
import com.tcmaster.entity.TestResult;
import com.tcmaster.entity.TestRun;
import com.tcmaster.entity.User;
import com.tcmaster.enums.TestResultStatus;
import com.tcmaster.enums.TestRunStatus;
import com.tcmaster.repository.ProjectRepository;
import com.tcmaster.repository.TestCaseRepository;
import com.tcmaster.repository.TestResultRepository;
import com.tcmaster.repository.TestRunRepository;
import com.tcmaster.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RunService {

    private final TestRunRepository testRunRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TestCaseRepository testCaseRepository;
    private final TestResultRepository testResultRepository;
    private final RunStreamingService runStreamingService;

    public RunService(
            TestRunRepository testRunRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository,
            TestCaseRepository testCaseRepository,
            TestResultRepository testResultRepository,
            RunStreamingService runStreamingService) {
        this.testRunRepository = testRunRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.testCaseRepository = testCaseRepository;
        this.testResultRepository = testResultRepository;
        this.runStreamingService = runStreamingService;
    }

    public RunDtos.RunResponse create(Long projectId, RunDtos.RunCreateRequest request, String userEmail) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        TestRun run = new TestRun();
        run.setProject(project);
        run.setTitle(request.title());
        run.setRunType(request.runType());
        run.setStatus(TestRunStatus.PENDING);
        run.setTriggeredBy(user);
        return RunDtos.RunResponse.from(testRunRepository.save(run));
    }

    public List<RunDtos.RunResponse> getRuns(Long projectId) {
        return testRunRepository.findByProjectIdOrderByIdDesc(projectId)
                .stream()
                .map(RunDtos.RunResponse::from)
                .toList();
    }

    public RunDtos.RunResponse getRun(Long runId) {
        return RunDtos.RunResponse.from(findRun(runId));
    }

    public RunDtos.RunResponse execute(Long runId) {
        TestRun run = findRun(runId);
        run.setStatus(TestRunStatus.RUNNING);
        run.setStartedAt(LocalDateTime.now());
        testRunRepository.save(run);
        runStreamingService.publish(run.getId(), "status", "Run started", run.getStatus());

        // Placeholder sandbox execution loop
        List<TestCase> candidates = testCaseRepository.findAll().stream()
                .filter(c -> c.getSuite().getProject().getId().equals(run.getProject().getId()))
                .toList();

        for (TestCase testCase : candidates) {
            TestResult result = new TestResult();
            result.setTestRun(run);
            result.setTestCase(testCase);
            result.setResult(TestResultStatus.PASS);
            result.setDurationMs(120);
            result.setLogOutput("Simulated execution for " + testCase.getTcId());
            testResultRepository.save(result);
            runStreamingService.publish(run.getId(), "log", result.getLogOutput(), run.getStatus());
        }

        run.setStatus(TestRunStatus.COMPLETED);
        run.setCompletedAt(LocalDateTime.now());
        testRunRepository.save(run);
        runStreamingService.publish(run.getId(), "status", "Run completed", run.getStatus());
        return RunDtos.RunResponse.from(run);
    }

    public RunDtos.RunResponse abort(Long runId) {
        TestRun run = findRun(runId);
        run.setStatus(TestRunStatus.ABORTED);
        run.setCompletedAt(LocalDateTime.now());
        testRunRepository.save(run);
        runStreamingService.publish(run.getId(), "status", "Run aborted", run.getStatus());
        return RunDtos.RunResponse.from(run);
    }

    public List<RunDtos.ResultResponse> getResults(Long runId) {
        return testResultRepository.findByTestRunIdOrderByIdAsc(runId).stream().map(RunDtos.ResultResponse::from).toList();
    }

    private TestRun findRun(Long runId) {
        return testRunRepository.findById(runId)
                .orElseThrow(() -> new IllegalArgumentException("Run not found"));
    }
}
