package com.tcmaster.controller;

import com.tcmaster.dto.RunDtos;
import com.tcmaster.service.RunService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestRunController {

    private final RunService runService;

    public TestRunController(RunService runService) {
        this.runService = runService;
    }

    @PostMapping("/api/projects/{id}/runs")
    public RunDtos.RunResponse createRun(
            @PathVariable Long id,
            @Valid @RequestBody RunDtos.RunCreateRequest request,
            Principal principal) {
        return runService.create(id, request, principal.getName());
    }

    @GetMapping("/api/projects/{id}/runs")
    public List<RunDtos.RunResponse> getRuns(@PathVariable Long id) {
        return runService.getRuns(id);
    }

    @GetMapping("/api/runs/{id}")
    public RunDtos.RunResponse getRun(@PathVariable Long id) {
        return runService.getRun(id);
    }

    @PostMapping("/api/runs/{id}/execute")
    public RunDtos.RunResponse execute(@PathVariable Long id) {
        return runService.execute(id);
    }

    @PostMapping("/api/runs/{id}/abort")
    public RunDtos.RunResponse abort(@PathVariable Long id) {
        return runService.abort(id);
    }

    @GetMapping("/api/runs/{id}/results")
    public List<RunDtos.ResultResponse> getResults(@PathVariable Long id) {
        return runService.getResults(id);
    }
}
