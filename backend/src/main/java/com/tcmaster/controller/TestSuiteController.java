package com.tcmaster.controller;

import com.tcmaster.dto.SuiteDtos;
import com.tcmaster.service.TestSuiteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestSuiteController {

    private final TestSuiteService testSuiteService;

    public TestSuiteController(TestSuiteService testSuiteService) {
        this.testSuiteService = testSuiteService;
    }

    @GetMapping("/api/projects/{id}/suites")
    public List<SuiteDtos.SuiteResponse> getSuites(@PathVariable Long id) {
        return testSuiteService.getSuites(id);
    }

    @PostMapping("/api/projects/{id}/suites")
    public SuiteDtos.SuiteResponse createSuite(@PathVariable Long id, @Valid @RequestBody SuiteDtos.SuiteRequest request) {
        return testSuiteService.create(id, request);
    }

    @PutMapping("/api/suites/{id}")
    public SuiteDtos.SuiteResponse updateSuite(@PathVariable Long id, @Valid @RequestBody SuiteDtos.SuiteRequest request) {
        return testSuiteService.update(id, request);
    }

    @DeleteMapping("/api/suites/{id}")
    public void deleteSuite(@PathVariable Long id) {
        testSuiteService.delete(id);
    }

    @PatchMapping("/api/suites/{id}/move")
    public SuiteDtos.SuiteResponse moveSuite(@PathVariable Long id, @RequestBody SuiteDtos.SuiteMoveRequest request) {
        return testSuiteService.move(id, request);
    }
}
